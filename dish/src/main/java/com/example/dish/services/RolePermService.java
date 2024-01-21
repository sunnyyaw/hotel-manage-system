package com.example.dish.services;

import com.example.dish.entity.*;
import com.example.dish.mapper.PermissionMapper;
import com.example.dish.mapper.RoleMapper;

import java.util.List;

public interface RolePermService extends RoleMapper, PermissionMapper {
    List<Role> getAllRoleInfo();
    void saveRole(Role role)throws Exception;
    void deleteRole(Long id)throws Exception;
    boolean existsByRoleName(Long id, String roleName);
    boolean existsByRoleId(Long id);
    List<Permission> getPermissionsByRole(Role role);
    List<Role_Permission> getRole_PermissionsByRole(Role role);
    List<Role_Permission> getRole_PermissionsByRoleId(Long id);
    List<User_Role> getUser_RolesById(Long id);
    void updateRole_PermissionsByRole(Role role);
    void deleteUser_RolesByRole(Long id);
    void deleteRole_PermissionsByRole(Long id);



    void savePermission(Permission permission)throws Exception;
    void addAllRole_Permission(Permission permission);
    void deletePermission(Long id)throws Exception;
    boolean matchesByURL(Long id, String url);
    boolean matchesByURL(String url);
    boolean existsByPermissionId(Long id);
    void deleteRole_PermissionsByPermission(Long id);
    List<Role_Permission> getRole_PermissionsByPermissionId(Long id);
}
