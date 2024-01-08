package com.example.dish.mapper;

import com.example.dish.entity.Permission;
import com.example.dish.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PermissionMapper {
    List<Permission> getAllPermissions();
    Permission getPermissionById(Long id);
    void addPermission(Permission permission);
    void deletePermissionById(Long id);
    void updatePermission(Permission permission);
}
