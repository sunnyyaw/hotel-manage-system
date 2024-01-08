package com.example.dish.mapper;

import com.example.dish.entity.Role;
import com.example.dish.entity.User_Role;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface User_RoleMapper {
    List<User_Role> getAllUser_Roles();
    void addUser_Role(User_Role user_role);
    void deleteUser_Role(User_Role user_role);
}
