package com.example.dish.services;

import com.example.dish.Exception.UserExistException;
import com.example.dish.Exception.UserNotFoundException;
import com.example.dish.entity.*;
import com.example.dish.mapper.*;
import com.example.dish.utils.PermissionUtils;
import com.example.dish.utils.RoleUtils;
import com.example.dish.utils.UserUtils;
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
    private UserUtils userMapper;
    @Autowired
    private RoleUtils roleMapper;
    @Autowired
    private PermissionUtils permissionMapper;
    @Autowired
    private User_RoleMapper user_roleMapper;
    @Autowired
    private Role_PermissionMapper role_permissionMapper;

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userMapper.getAllUsers();
        return users.stream().map(user->{
            List<Role> roles = userMapper.getRolesByUser(user);
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
        User user = userMapper.getUserByUsername(username);
        if(Objects.isNull(user)){
            throw new UserNotFoundException();
        }
        List<Role> roles = userMapper.getRolesByUser(user);
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setRoles(roles);
        return userDTO;
    }

    @Override
    @Transactional
    public void saveUser(UserDTO userDTO)throws Exception {
        if(userMapper.existsByUsername(userDTO.getId(),userDTO.getUsername()))
            throw new Exception("用户名已存在");
        if(!userMapper.existsById(userDTO.getId())){
            userMapper.encodePassword(userDTO);
            userMapper.addUser(userDTO);
        }
        else{
            if(userDTO.getNeedEncode())
                userMapper.encodePassword(userDTO);
            userMapper.updateUser(userDTO);
        }
        userMapper.updateUser_RolesByUser(userDTO);
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
    public void logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
    }

    @Override
    public void register(User user) throws Exception {
        String username = user.getUsername();
        username = HtmlUtils.htmlEscape(username);
        if(userMapper.existsByUsername(user.getId(),user.getUsername())){
            throw new UserExistException();
        }
        user.setUsername(username);
        userMapper.encodePassword(user);
        userMapper.addUser(user);
    }

    @Override
    public void modifyPassword(String password) throws Exception {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        User user= userMapper.getUserByUsername(username);
        user.setPassword(password);
        userMapper.encodePassword(user);
        userMapper.updateUser(user);
    }

    @Override
    public List<RoleDTO> getAllRoles() {
        return roleMapper.getAllRoles().stream()
                .map(role->{
                    List<Permission> permissions = roleMapper.getPermissionsByRole(role);
                    RoleDTO roleDTO = new RoleDTO();
                    roleDTO.setId(role.getId());
                    roleDTO.setRoleName(role.getRoleName());
                    roleDTO.setPermissions(permissions);
                    return roleDTO;
                }).toList();
    }

    @Override
    @Transactional
    public void saveRole(RoleDTO roleDTO)throws Exception {
        roleDTO.setRoleName(roleDTO.getRoleName().trim());
        if(roleMapper.existsByRoleName(roleDTO.getId(),roleDTO.getRoleName()))
            throw new Exception("角色名已存在");
        if (!roleMapper.existsById(roleDTO.getId()))
            roleMapper.addRole(roleDTO);
        else
            roleMapper.updateRole(roleDTO);
        roleMapper.updateRole_PermissionsByRole(roleDTO);
    }

    @Override
    @Transactional
    public void deleteRole(Long id) throws Exception {
        if(!roleMapper.existsById(id))
            throw new Exception("角色不存在");
        roleMapper.deleteUser_RolesByRole(id);
        roleMapper.deleteRole_PermissionsByRole(id);
        roleMapper.deleteRoleById(id);
    }

    @Override
    public List<Permission> getAllPermissions() {
        return permissionMapper.getAllPermissions();
    }

    @Override
    public void savePermission(Permission permission) throws Exception {
        if(permissionMapper.existsByURL(permission.getId(),permission.getUrl()))
            throw new Exception("url已存在");
        if(!permissionMapper.existsById(permission.getId())){
            permissionMapper.addPermission(permission);
        }
        else{
            permissionMapper.updatePermission(permission);
        }
    }


    @Override
    @Transactional
    public void deletePermission(Long id) throws Exception {
        if(!permissionMapper.existsById(id))
            throw new Exception("权限不存在");
        permissionMapper.deleteRole_PermissionsByPermission(id);
        permissionMapper.deletePermissionById(id);
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
        userMapper.deleteUser_RolesByUser(id);
        userMapper.deleteUser(user);
    }
}
