package com.example.dish.services;

import com.example.dish.Exception.PasswordCollideException;
import com.example.dish.Exception.UserExistException;
import com.example.dish.Exception.UserNotFoundException;
import com.example.dish.entity.*;
import com.example.dish.mapper.*;
import com.example.dish.utils.PasswordUtils;
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
    private RoleService roleService;
    @Autowired
    private User_RoleMapper user_roleMapper;
    @Autowired
    private Role_PermissionMapper role_permissionMapper;

    @Override
    public List<UserDTO> getAllUserInfo() {
        List<User> users = this.getAllUsers();
        return users.stream().map(user->{
            List<Role> roles = this.getRolesByUser(user);
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setUsername(user.getUsername());
            userDTO.setPassword(user.getPassword());
            userDTO.setSalt(user.getSalt());
            userDTO.setRoles(roles);
            return userDTO;
        }).toList();
    }

    @Override
    public UserDTO getUserInfo() throws UserNotFoundException {
        Subject subject = SecurityUtils.getSubject();
        String username = subject.getPrincipal().toString();
        User user = this.getUserByUsername(username);
        if(Objects.isNull(user)){
            throw new UserNotFoundException();
        }
        List<Role> roles = this.getRolesByUser(user);
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setPhone(user.getPhone());
        userDTO.setSalt(user.getSalt());
        userDTO.setRoles(roles);
        return userDTO;
    }

    @Override
    @Transactional
    public void saveUser(UserDTO userDTO)throws Exception {
        if(this.existsByUsername(userDTO.getId(),userDTO.getUsername()))
            throw new Exception("用户名已存在");
        if(!this.existsById(userDTO.getId())){
            this.encodePassword(userDTO);
            this.addUser(userDTO);
        }
        else{
            if(userDTO.getNeedEncode())
                this.encodePassword(userDTO);
            this.updateUser(userDTO);
        }
        this.updateUser_RolesByUser(userDTO);
    }

    @Override
    public void login(UserDTO userForm)throws AuthenticationException {
        String username = userForm.getUsername();
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username,userForm.getPassword());
        usernamePasswordToken.setRememberMe(userForm.getRememberMe());
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
        User user = this.getUserByUsername(phone);
        if(user == null){
            user = new User();
            user.setUsername(phone);
            user.setPassword(phone);
            user.setPhone(phone);
            this.encodePassword(user);
            this.addUser(user);
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
    public void register(User user) throws Exception {
        String username = user.getUsername();
        username = HtmlUtils.htmlEscape(username);
        if(this.existsByUsername(user.getId(),user.getUsername())){
            throw new UserExistException();
        }
        user.setUsername(username);
        this.encodePassword(user);
        this.addUser(user);
    }

    @Override
    public void modifyPassword(String password) throws Exception {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        User user= this.getUserByUsername(username);
        user.setPassword(password);
        this.encodePassword(user);
        this.updateUser(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id)throws Exception {
        User user = this.getUserById(id);
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        if(Objects.isNull(user))
            throw new UserNotFoundException();
        if(Objects.equals(username,user.getUsername()))
            throw new Exception("不能删除自己");
        this.deleteUser_RolesByUser(id);
        this.deleteUser(user);
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
        return this.getAllUsers().stream()
                .anyMatch(u->!Objects.equals(u.getId(),id)&&
                        Objects.equals(u.getUsername(),username));
    }
    @Override
    public boolean existsById(Long id) {
        return !Objects.isNull(this.getUserById(id));
    }

    @Override
    public void deleteUser_RolesByUser(Long id) {
        this.getUser_RolesById(id)
                .forEach(user_role->user_roleMapper.deleteUser_Role(user_role));
    }

    @Override
    public void updateUser_RolesByUser(UserDTO userDTO) {
        Long userId = userDTO.getId();
        List<Role> newRoles = userDTO.getRoles();
        List<User_Role> user_roles = this.getUser_RolesByUser(userDTO);
        user_roles.forEach(user_role -> {
            if(newRoles.stream()
                    .noneMatch(newRole-> Objects.equals(newRole.getId(),user_role.getRoleId())))
                user_roleMapper.deleteUser_Role(user_role);
        });
        newRoles.forEach(newRole->{
            if(user_roles.stream()
                    .noneMatch(user_role -> Objects.equals(newRole.getId(),user_role.getRoleId()))){
                User_Role user_role = new User_Role();
                user_role.setUserId(userId);
                user_role.setRoleId(newRole.getId());
                user_roleMapper.addUser_Role(user_role);
            }
        });
    }

    @Override
    public void encodePassword(User userDTO) throws Exception{
        User user= this.getUserByUsername(userDTO.getUsername());
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
    public List<User> getAllUsers() {
        return userMapper.getAllUsers();
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
    public User getUserByPhone(String phone) {
        return userMapper.getUserByPhone(phone);
    }

    @Override
    public void addUser(User user) {
        userMapper.addUser(user);
    }

    @Override
    public void deleteUser(User user) {
        userMapper.deleteUser(user);
    }

    @Override
    public void updateUser(User user) {
        userMapper.updateUser(user);
    }
}
