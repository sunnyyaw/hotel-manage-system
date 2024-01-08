package com.example.dish.utils;

import com.example.dish.entity.*;
import com.example.dish.mapper.PermissionMapper;
import com.example.dish.mapper.RoleMapper;
import com.example.dish.mapper.Role_PermissionMapper;
import com.example.dish.mapper.User_RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class RoleUtilsImpl implements RoleUtils{
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private Role_PermissionMapper role_permissionMapper;
    @Autowired
    private User_RoleMapper user_roleMapper;
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
    public boolean existsByRoleName(Long id, String roleName) {
        return roleMapper.getAllRoles().stream().anyMatch(role1 ->
                !Objects.equals(role1.getId(),id)&&
                        role1.getRoleName().equals(roleName));
    }

    @Override
    public boolean existsById(Long id) {
        return !Objects.isNull(roleMapper.getRoleById(id));
    }

    @Override
    public List<Permission> getPermissionsByRole(Role role) {
        return this.getRole_PermissionsByRole(role).stream()
                .map(role_permission -> permissionMapper.getPermissionById(role_permission.getPermissionId()))
                .toList();
    }

    @Override
    public List<Role_Permission> getRole_PermissionsByRole(Role role) {
        return role_permissionMapper.getAllRole_Permissions().stream()
                .filter(role_permission -> Objects.equals(role.getId(),role_permission.getRoleId()))
                .toList();
    }

    @Override
    public List<Role_Permission> getRole_PermissionsById(Long id) {
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
    public void updateRole_PermissionsByRole(RoleDTO roleDTO) {
        List<Role_Permission> role_permissions = this.getRole_PermissionsByRole(roleDTO);
        List<Permission> newPermissions= roleDTO.getPermissions();
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
                role_permission.setRoleId(roleDTO.getId());
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
        this.getRole_PermissionsById(id).forEach(role_permission ->
                        role_permissionMapper.deleteRole_Permission(role_permission));
    }
}
