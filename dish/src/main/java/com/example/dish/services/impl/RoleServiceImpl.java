package com.example.dish.services.impl;

import com.example.dish.entity.*;
import com.example.dish.mapper.PermissionMapper;
import com.example.dish.mapper.RoleMapper;
import com.example.dish.mapper.Role_PermissionMapper;
import com.example.dish.mapper.User_RoleMapper;
import com.example.dish.services.RolePermService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Service
public class RoleServiceImpl implements RolePermService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private Role_PermissionMapper role_permissionMapper;
    @Autowired
    private User_RoleMapper user_roleMapper;

    @Override
    public List<Role> getAllRoleInfo() {
        return this.getAllRoles().stream()
                .peek(role->{
                    List<Permission> permissions = this.getPermissionsByRole(role);
                    role.setPermissions(permissions);
                }).toList();
    }

    @Override
    @Transactional
    public void saveRole(Role role)throws Exception {
        role.setRoleName(role.getRoleName().trim());
        if(this.existsByRoleName(role.getId(),role.getRoleName()))
            throw new Exception("角色名已存在");
        if (!this.existsByRoleId(role.getId()))
            this.addRole(role);
        else
            this.updateRole(role);
        this.updateRole_PermissionsByRole(role);
    }

    @Override
    @Transactional
    public void deleteRole(Long id) throws Exception {
        if(!this.existsByRoleId(id))
            throw new Exception("角色不存在");
        this.deleteUser_RolesByRole(id);
        this.deleteRole_PermissionsByRole(id);
        this.deleteRoleById(id);
    }
    @Override
    public boolean existsByRoleName(Long id, String roleName) {
        return this.getAllRoles().stream().anyMatch(role1 ->
                !Objects.equals(role1.getId(),id)&&
                        role1.getRoleName().equals(roleName));
    }

    @Override
    public boolean existsByRoleId(Long id) {
        return !Objects.isNull(this.getRoleById(id));
    }

    @Override
    public List<Permission> getPermissionsByRole(Role role) {
        return this.getRole_PermissionsByRole(role).stream()
                .map(role_permission -> this.getPermissionById(role_permission.getPermissionId()))
                .toList();
    }

    @Override
    public List<Role_Permission> getRole_PermissionsByRole(Role role) {
        return role_permissionMapper.getAllRole_Permissions().stream()
                .filter(role_permission -> Objects.equals(role.getId(),role_permission.getRoleId()))
                .toList();
    }

    @Override
    public List<Role_Permission> getRole_PermissionsByRoleId(Long id) {
        return role_permissionMapper.getAllRole_Permissions().stream()
                .filter(role_permission -> Objects.equals(id,role_permission.getRoleId()))
                .toList();
    }

    @Override
    public List<User_Role> getUser_RolesById(Long id) {
        return user_roleMapper.getAllUser_Roles().stream()
                .filter(user_role -> Objects.equals(id,user_role.getRoleId()))
                .toList();
    }

    @Override
    public void updateRole_PermissionsByRole(Role role) {
        List<Role_Permission> role_permissions = this.getRole_PermissionsByRole(role);
        List<Permission> newPermissions= role.getPermissions();
        role_permissions.forEach(role_permission->{
            if(newPermissions.stream().noneMatch(permission1 ->
                    Objects.equals(permission1.getId(),role_permission.getPermissionId())))
                role_permissionMapper.deleteRole_Permission(role_permission);
        });
        newPermissions.forEach(permission->{
            if(role_permissions.stream().noneMatch(role_permission->
                    Objects.equals(role_permission.getPermissionId(),permission.getId()))){
                Role_Permission role_permission = new Role_Permission();
                role_permission.setPermissionId(permission.getId());
                role_permission.setRoleId(role.getId());
                role_permissionMapper.addRole_Permission(role_permission);
            }
        });
    }

    @Override
    public void deleteUser_RolesByRole(Long id) {
        this.getUser_RolesById(id).forEach(user_role ->
                user_roleMapper.deleteUser_Role(user_role));
    }

    @Override
    public void deleteRole_PermissionsByRole(Long id) {
        this.getRole_PermissionsByRoleId(id).forEach(role_permission ->
                role_permissionMapper.deleteRole_Permission(role_permission));
    }

    @Override
    public List<Role> getAllRoles() {
        return roleMapper.getAllRoles();
    }

    @Override
    public Role getRoleById(Long id) {
        return roleMapper.getRoleById(id);
    }

    @Override
    public void addRole(Role role) {
        roleMapper.addRole(role);
    }

    @Override
    public void deleteRoleById(Long id) {
        roleMapper.deleteRoleById(id);
    }

    @Override
    public void updateRole(Role role) {
        roleMapper.updateRole(role);
    }


    @Override
    public void savePermission(Permission permission) throws Exception {
        if(this.matchesByURL(permission.getId(),permission.getUrl()))
            throw new Exception("url已存在");
        if(!this.existsByPermissionId(permission.getId())){
            this.addPermission(permission);
            this.addAllRole_Permission(permission);
        }
        else{
            this.updatePermission(permission);
        }
    }

    @Override
    @Transactional
    public void addAllRole_Permission(Permission permission) {
        this.getAllRoles().forEach(
                role->role_permissionMapper.addRole_Permission(
                        Role_Permission.builder()
                                .permissionId(permission.getId())
                                .roleId(role.getId())
                                .build()));
    }


    @Override
    @Transactional
    public void deletePermission(Long id) throws Exception {
        if(!this.existsByPermissionId(id))
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
    public boolean matchesByURL(Long id, String url) {
        return this.getAllPermissions().stream()
                .anyMatch(perm-> !Objects.equals(perm.getId(),id)
                        && Objects.equals(perm.getUrl(),url));
    }

    @Override
    public boolean matchesByURL(String url) {
        return this.getAllPermissions().stream()
                .anyMatch(perm-> Pattern.matches(perm.getUrl(),url));
    }

    @Override
    public boolean existsByPermissionId(Long id) {
        return !Objects.isNull(this.getPermissionById(id));
    }


    @Override
    public void deleteRole_PermissionsByPermission(Long id) {
        this.getRole_PermissionsByPermissionId(id)
                .forEach(role_perm->role_permissionMapper.deleteRole_Permission(role_perm));
    }
    @Override
    public List<Role_Permission> getRole_PermissionsByPermissionId(Long id) {
        return role_permissionMapper.getAllRole_Permissions().stream()
                .filter(role_perm->Objects.equals(role_perm.getPermissionId(),id))
                .toList();
    }
}
