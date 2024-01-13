package com.example.dish.services;

import com.example.dish.Exception.UserNotFoundException;
import com.example.dish.entity.*;
import com.example.dish.mapper.UserMapper;
import org.apache.shiro.authc.AuthenticationException;

import java.util.List;

public interface UserService extends UserMapper {
    List<UserDTO> getAllUserInfo();
    UserDTO getUserInfo()throws UserNotFoundException;
    void saveUser(UserDTO userDTO)throws Exception;
    void login(UserDTO userForm)throws AuthenticationException;
    void phoneLogin(UserDTO userDTO) throws Exception;
    void logout();
    void register(User user)throws Exception;
    void modifyPassword(String password)throws Exception;
    void deleteUser(Long id)throws Exception;

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
