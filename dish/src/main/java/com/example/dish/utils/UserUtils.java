package com.example.dish.utils;

import com.example.dish.entity.*;
import com.example.dish.mapper.UserMapper;

import java.util.List;
public interface UserUtils extends UserMapper {
    List<Role> getRolesByUser(User user);
    List<Permission> getPermissionsByUser(User user);
    List<User_Role> getUser_RolesByUser(User user);
    List<User_Role> getUser_RolesById(Long id);
    boolean existsByUsername(Long id, String username);
    boolean existsById(Long id);
    void deleteUser_RolesByUser(Long id);
    void updateUser_RolesByUser(UserDTO userDTO);
    void encodePassword(User user) throws Exception;
}
