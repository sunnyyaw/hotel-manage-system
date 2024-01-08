package com.example.dish.mapper;

import com.example.dish.entity.Role_Permission;
import com.example.dish.entity.User_Role;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface Role_PermissionMapper {
    List<Role_Permission> getAllRole_Permissions();
    void addRole_Permission(Role_Permission role_permission);
    void deleteRole_Permission(Role_Permission role_permission);
}
