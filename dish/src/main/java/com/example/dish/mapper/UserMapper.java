package com.example.dish.mapper;

import com.example.dish.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {
    List<User> getAllUsers();
    User getUserById(Long id);
    User getUserByUsername(String username);
    User getUserByPhone(String phone);
    void addUser(User user);
    void deleteUser(User user);
    void updateUser(User user);
}
