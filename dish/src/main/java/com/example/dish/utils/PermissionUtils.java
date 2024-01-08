package com.example.dish.utils;

import com.example.dish.entity.Role_Permission;
import com.example.dish.mapper.PermissionMapper;

import java.util.List;

public interface PermissionUtils extends PermissionMapper {
    boolean existsByURL(Long id, String url);
    boolean existsById(Long id);
    boolean anyMatchesURL(String url);
    void deleteRole_PermissionsByPermission(Long id);
    List<Role_Permission> getRole_PermissionsById(Long id);
}
