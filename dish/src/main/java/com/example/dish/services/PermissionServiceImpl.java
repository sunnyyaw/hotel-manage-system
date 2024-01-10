package com.example.dish.services;

import com.example.dish.entity.Permission;
import com.example.dish.entity.Role_Permission;
import com.example.dish.mapper.PermissionMapper;
import com.example.dish.mapper.Role_PermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
@Service
public class PermissionServiceImpl implements PermissionService{

    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private Role_PermissionMapper role_permissionMapper;

    @Override
    public List<Permission> getAllPermissionInfo() {
        return this.getAllPermissions();
    }

    @Override
    public void savePermission(Permission permission) throws Exception {
        if(this.existsByURL(permission.getId(),permission.getUrl()))
            throw new Exception("url已存在");
        if(!this.existsById(permission.getId())){
            this.addPermission(permission);
        }
        else{
            this.updatePermission(permission);
        }
    }


    @Override
    @Transactional
    public void deletePermission(Long id) throws Exception {
        if(!this.existsById(id))
            throw new Exception("权限不存在");
        this.deleteRole_PermissionsByPermission(id);
        this.deletePermissionById(id);
    }
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
        return this.getAllPermissions().stream()
                .anyMatch(perm-> !Objects.equals(perm.getId(),id)
                        && Objects.equals(perm.getUrl(),url.trim()));
    }

    @Override
    public boolean existsById(Long id) {
        return !Objects.isNull(this.getPermissionById(id));
    }

    @Override
    public boolean anyMatchesURL(String url) {
        return this.getAllPermissions().stream()
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
