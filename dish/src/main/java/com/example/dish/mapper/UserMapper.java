package com.example.dish.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.dish.common.Query;
import com.example.dish.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper{
    List<User> getAllUsers();
    List<User> listUsers(Query query);
    int count(Query query);
    User getUserById(Long id);
    User getUserByUsername(String username);
    User getUserByPhone(String phone);
    void addUser(User user);
    void deleteUser(User user);
    void deleteUsers(List<Long> ids);
    void updateUser(User user);
}
