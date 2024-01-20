package com.example.dish.services;

import com.example.dish.Exception.UserNotFoundException;
import com.example.dish.common.Query;
import com.example.dish.entity.*;
import org.apache.shiro.authc.AuthenticationException;

import java.util.List;

public interface UserService{
    List<User> listUsers(Query query);
    User getUserInfo()throws UserNotFoundException;
    void saveUser(UserDTO userDTO)throws Exception;
    void login(User user)throws AuthenticationException;
    void phoneLogin(UserDTO userDTO) throws Exception;
    void logout();
    void register(User user)throws Exception;
    void deleteUser(Long id)throws Exception;
    int count(Query query);
    List<User> getAllUsers();
    User get(Long id)throws Exception;
    User getUserById(Long id);
    User getUserByUsername(String username);
    void addUser(User user);
    void deleteUsers(List<Long> ids);
    void updateUser(User user)throws Exception;

    List<Role> getRolesByUser(User user);
    List<Permission> getPermissionsByUser(User user);
    List<User_Role> getUser_RolesByUser(User user);
    List<User_Role> getUser_RolesById(Long id);
    boolean existsByUsername(Long id, String username);
    boolean existsById(Long id);
    void deleteUser_RolesByUser(Long id);
    void updateUser_RolesByUser(User user);
    void encodePassword(User user) throws Exception;
}
