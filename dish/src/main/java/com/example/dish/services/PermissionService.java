package com.example.dish.services;

import com.example.dish.entity.Permission;
import com.example.dish.entity.Role_Permission;
import com.example.dish.mapper.PermissionMapper;

import java.util.List;

public interface PermissionService extends PermissionMapper {
    List<Permission> getAllPermissionInfo();
    void savePermission(Permission permission)throws Exception;
    void deletePermission(Long id)throws Exception;
    boolean existsByURL(Long id, String url);
    boolean existsById(Long id);
    boolean anyMatchesURL(String url);
    void deleteRole_PermissionsByPermission(Long id);
    List<Role_Permission> getRole_PermissionsById(Long id);

}
