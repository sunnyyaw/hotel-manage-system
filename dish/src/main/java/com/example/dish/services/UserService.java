package com.example.dish.services;

import com.example.dish.Exception.UserExistException;
import com.example.dish.Exception.UserNotFoundException;
import com.example.dish.entity.*;
import org.apache.shiro.authc.AuthenticationException;

import java.util.List;

public interface UserService {
    List<UserDTO> getAllUsers();
    UserDTO getUserInfo()throws UserNotFoundException;
    void saveUser(UserDTO userDTO)throws Exception;
    void login(UserDTO userForm)throws AuthenticationException;
    void logout();
    void register(User user)throws Exception;
    void modifyPassword(String password)throws Exception;
    List<RoleDTO> getAllRoles();
    void saveRole(RoleDTO roleDTO)throws Exception;
    void deleteRole(Long id)throws Exception;
    List<Permission> getAllPermissions();
    void savePermission(Permission permission)throws Exception;
    void deletePermission(Long id)throws Exception;
    void deleteUser(Long id)throws Exception;
}
