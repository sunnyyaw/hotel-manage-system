package com.example.dish.services.impl;

import com.example.dish.Exception.PasswordCollideException;
import com.example.dish.Exception.UserExistException;
import com.example.dish.Exception.UserNotFoundException;
import com.example.dish.common.Query;
import com.example.dish.entity.*;
import com.example.dish.dto.UserDTO;
import com.example.dish.mapper.*;
import com.example.dish.services.RolePermService;
import com.example.dish.services.UserService;
import com.example.dish.common.PasswordUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;
import java.util.Objects;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RolePermService roleService;
    @Autowired
    private User_RoleMapper user_roleMapper;
    @Autowired
    private Role_PermissionMapper role_permissionMapper;

    @Override
    public List<User> listUsers(Query query) {
        List<User> users = userMapper.listUsers(query);
        return users.stream().peek(user->{
            List<Role> roles = this.getRolesByUser(user);
            user.setRoles(roles);
        }).toList();
    }

    @Override
    public User getUserInfo() throws UserNotFoundException {
        Subject subject = SecurityUtils.getSubject();
        String username = subject.getPrincipal().toString();
        User user = userMapper.getUserByUsername(username);
        if(Objects.isNull(user)){
            throw new UserNotFoundException();
        }
        List<Role> roles = this.getRolesByUser(user);
        user.setRoles(roles);
        return user;
    }

    @Override
    @Transactional
    public void saveUser(UserDTO userDTO)throws Exception {
        if(this.existsByUsername(userDTO.getId(),userDTO.getUsername()))
            throw new Exception("用户名已存在");
        if(!this.existsById(userDTO.getId())){
            this.encodePassword(userDTO);
            userMapper.addUser(userDTO);
        }
        else{
            if(userDTO.getNeedEncode())
                this.encodePassword(userDTO);
            userMapper.updateUser(userDTO);
        }

        List<Role> newRoles = userDTO.getRoles();
        if(newRoles == null)return;
        Query query = new Query();
        query.put("userId",userDTO.getId());
        List<User_Role> user_roles = user_roleMapper.all(query);
        user_roles.forEach(user_role ->
                user_roleMapper.deleteUser_Role(user_role));
        newRoles.forEach(newRole->{
            User_Role user_role = new User_Role();
            user_role.setUserId(userDTO.getId());
            user_role.setRoleId(newRole.getId());
            user_roleMapper.addUser_Role(user_role);
        });
    }

    @Override
    public void login(User user)throws AuthenticationException {
        String username = user.getUsername();
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username,user.getPassword());
        usernamePasswordToken.setRememberMe(user.getRememberMe());
        try{
            subject.login(usernamePasswordToken);
        }catch(AuthenticationException e){
            throw e;
        }
    }

    @Override
    @Transactional
    public void phoneLogin(UserDTO userDTO) throws Exception {
        String phone = userDTO.getPhone();
        User user = userMapper.getUserByUsername(phone);
        if(user == null){
            user = new User();
            user.setUsername(phone);
            user.setPassword(phone);
            user.setPhone(phone);
            this.encodePassword(user);
            userMapper.addUser(user);
        }
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(user.getPhone(),user.getPhone());
        usernamePasswordToken.setRememberMe(userDTO.getRememberMe());
        try{
            subject.login(usernamePasswordToken);
        }catch(AuthenticationException e){
            throw e;
        }
    }

    @Override
    public void logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
    }

    @Override
    @Transactional
    public void register(User user) throws Exception {
        String username = user.getUsername();
        String password = user.getPassword();
        username = HtmlUtils.htmlEscape(username);
        if(this.existsByUsername(user.getId(),user.getUsername())){
            throw new UserExistException();
        }
        user.setUsername(username);
        this.encodePassword(user);
        userMapper.addUser(user);
        user.setPassword(password);
        this.login(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id)throws Exception {
        User user = userMapper.getUserById(id);
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        if(Objects.isNull(user))
            throw new UserNotFoundException();
        if(Objects.equals(username,user.getUsername()))
            throw new Exception("不能删除自己");
        this.deleteUser_RolesByUser(id);
        userMapper.deleteUser(user);
    }

    @Override
    public List<Role> getRolesByUser(User user) {
        return this.getUser_RolesByUser(user).stream()
                .map(user_role ->
                        roleService.getRoleById(user_role.getRoleId())).toList();
    }

    @Override
    public List<Permission> getPermissionsByUser(User user) {
        return this.getRolesByUser(user).stream().flatMap(role->
                roleService.getPermissionsByRole(role).stream()
        ).toList();
    }

    @Override
    public List<User_Role> getUser_RolesByUser(User user) {
        return user_roleMapper.getAllUser_Roles()
                .stream().filter(user_role ->
                        Objects.equals(user_role.getUserId(),user.getId())
                ).toList();
    }

    @Override
    public List<User_Role> getUser_RolesById(Long id) {
        return user_roleMapper.getAllUser_Roles()
                .stream().filter(user_role ->
                        Objects.equals(user_role.getUserId(),id)
                ).toList();
    }

    @Override
    public boolean existsByUsername(Long id, String username) {
        return userMapper.getAllUsers().stream()
                .anyMatch(u->!Objects.equals(u.getId(),id)&&
                        Objects.equals(u.getUsername(),username));
    }
    @Override
    public boolean existsById(Long id) {
        return !Objects.isNull(userMapper.getUserById(id));
    }

    @Override
    public void deleteUser_RolesByUser(Long id) {
        this.getUser_RolesById(id)
                .forEach(user_role->user_roleMapper.deleteUser_Role(user_role));
    }

    @Override
    public void encodePassword(User userDTO) throws Exception{
        User user= userMapper.getUserByUsername(userDTO.getUsername());
        if(!Objects.isNull(user)&&
                PasswordUtils.encodePassword(userDTO.getPassword(),user.getSalt()).equals(user.getPassword())){
            throw new PasswordCollideException();
        }
        String salt = PasswordUtils.generateSalt();
        String encodedPassword = PasswordUtils.encodePassword(userDTO.getPassword(),salt);
        userDTO.setSalt(salt);
        userDTO.setPassword(encodedPassword);
    }

    @Override
    public int count(Query query) {
        return userMapper.count(query);
    }

    @Override
    public User get(Long id)throws Exception {
        User user = userMapper.getUserById(id);
        if(Objects.isNull(user))
            throw new Exception("用户不存在");
        List<Role> roles = this.getRolesByUser(user);
        user.setRoles(roles);
        return user;
    }

    @Override
    public User getUserById(Long id) {
        return userMapper.getUserById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }

    @Override
    @Transactional
    public void updateUser(User user)throws Exception {
        if(!this.existsById(user.getId()))
            throw new Exception("用户不存在");
        if(this.existsByUsername(user.getId(),user.getUsername()))
            throw new Exception("用户名已存在");
        if(user.getNeedEncode())
            this.encodePassword(user);
        userMapper.updateUser(user);

        List<Role> newRoles = user.getRoles();
        if(newRoles == null)return;
        Query query = new Query();
        query.put("userId",user.getId());
        List<User_Role> user_roles = user_roleMapper.all(query);
        user_roles.forEach(user_role ->
                user_roleMapper.deleteUser_Role(user_role));
        newRoles.forEach(newRole->{
            User_Role user_role = new User_Role();
            user_role.setUserId(user.getId());
            user_role.setRoleId(newRole.getId());
            user_roleMapper.addUser_Role(user_role);
        });
    }
}
