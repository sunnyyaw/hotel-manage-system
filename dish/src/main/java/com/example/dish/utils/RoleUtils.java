package com.example.dish.utils;

import com.example.dish.entity.*;
import com.example.dish.mapper.RoleMapper;

import java.util.List;

public interface RoleUtils extends RoleMapper {
    boolean existsByRoleName(Long id, String roleName);
    boolean existsById(Long id);
    List<Permission> getPermissionsByRole(Role role);
    List<Role_Permission> getRole_PermissionsByRole(Role role);
    List<Role_Permission> getRole_PermissionsById(Long id);
    List<User_Role> getUser_RolesById(Long id);
    void updateRole_PermissionsByRole(RoleDTO roleDTO);
    void deleteUser_RolesByRole(Long id);
    void deleteRole_PermissionsByRole(Long id);
}

