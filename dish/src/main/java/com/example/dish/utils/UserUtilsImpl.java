package com.example.dish.utils;

import com.example.dish.Exception.PasswordCollideException;
import com.example.dish.Exception.UserNotFoundException;
import com.example.dish.entity.*;
import com.example.dish.mapper.RoleMapper;
import com.example.dish.mapper.UserMapper;
import com.example.dish.mapper.User_RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserUtilsImpl implements UserUtils{
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleUtils roleMapper;
    @Autowired
    private User_RoleMapper user_roleMapper;
    @Override
    public List<Role> getRolesByUser(User user) {
        return this.getUser_RolesByUser(user).stream()
                .map(user_role ->
                        roleMapper.getRoleById(user_role.getRoleId())).toList();
    }

    @Override
    public List<Permission> getPermissionsByUser(User user) {
        return this.getRolesByUser(user).stream().flatMap(role->
            roleMapper.getPermissionsByRole(role).stream()
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
