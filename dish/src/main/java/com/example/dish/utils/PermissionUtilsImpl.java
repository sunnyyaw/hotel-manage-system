package com.example.dish.utils;

import com.example.dish.entity.Permission;
import com.example.dish.entity.Role_Permission;
import com.example.dish.mapper.PermissionMapper;
import com.example.dish.mapper.Role_PermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Service
public class PermissionUtilsImpl implements PermissionUtils{
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private Role_PermissionMapper role_permissionMapper;
    @Override
    public List<Permission> getAllPermissions() {
        return permissionMapper.getAllPermissions();
    }

    @Override
    public Permission getPermissionById(Long id) {
        return permissionMapper.getPermissionById(id);
    }

    @Override
    public void addPermission(Permission permission) {
        permissionMapper.addPermission(permission);
    }

    @Override
    public void deletePermissionById(Long id) {
        permissionMapper.deletePermissionById(id);
    }

    @Override
    public void updatePermission(Permission permission) {
        permissionMapper.updatePermission(permission);
    }

    @Override
    public boolean existsByURL(Long id, String url) {
        return permissionMapper.getAllPermissions().stream()
                .anyMatch(perm-> !Objects.equals(perm.getId(),id)
                        && Objects.equals(perm.getUrl(),url.trim()));
    }

    @Override
    public boolean existsById(Long id) {
        return !Objects.isNull(permissionMapper.getPermissionById(id));
    }

    @Override
    public boolean anyMatchesURL(String url) {
        return permissionMapper.getAllPermissions().stream()
                .anyMatch(permission -> Pattern.matches(permission.getUrl(),url));
    }

    @Override
    public void deleteRole_PermissionsByPermission(Long id) {
        this.getRole_PermissionsById(id)
                .forEach(role_perm->role_permissionMapper.deleteRole_Permission(role_perm));
    }

    @Override
    public List<Role_Permission> getRole_PermissionsById(Long id) {
        return role_permissionMapper.getAllRole_Permissions().stream()
                .filter(role_perm->Objects.equals(role_perm.getPermissionId(),id))
                .toList();
    }
}
