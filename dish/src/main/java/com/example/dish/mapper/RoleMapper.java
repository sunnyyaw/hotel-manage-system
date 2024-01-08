package com.example.dish.mapper;

import com.example.dish.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface RoleMapper {
    List<Role> getAllRoles();
    Role getRoleById(Long id);
    void addRole(Role role);
    void deleteRoleById(Long id);
    void updateRole(Role role);
}
