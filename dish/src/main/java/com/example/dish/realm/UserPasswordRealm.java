package com.example.dish.realm;

import com.example.dish.common.UserStatus;
import com.example.dish.entity.Permission;
import com.example.dish.entity.Role;
import com.example.dish.entity.User;
import com.example.dish.services.RolePermService;
import com.example.dish.services.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class UserPasswordRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;
    @Autowired
    private RolePermService rolePermService;
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = principalCollection.getPrimaryPrincipal().toString();

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        User user = userService.getUserByUsername(username);
        Set<String> roleSet = user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toSet());
        Set<String> permissionSet = user.getPermissions().stream().map(Permission::getUrl).collect(Collectors.toSet());
        authorizationInfo.setRoles(roleSet);
        authorizationInfo.setStringPermissions(permissionSet);
        /*
        if(!rolePermService.matchesByURL(httpServletRequest.getRequestURI()))
            return true;
        User user = userService.getUserByUsername(subject.getPrincipal().toString());
        if(user==null)
            throw new Exception("用户不存在");
        List<Role> roles = userService.getRolesByUser(user);
        List<Permission> permissions = userService.getPermissionsByUser(user);
        if(permissions.stream().noneMatch(permission->
                Pattern.matches(permission.getUrl(),httpServletRequest.getRequestURI())))
            throw new Exception("您没有访问权限");
        authorizationInfo.setRoles();
         */
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) {
        String username = authenticationToken.getPrincipal().toString();
        User user = userService.getUserByUsername(username);
        if(Objects.isNull(user)){
            throw new AuthenticationException("用户不存在");
        }
        if(user.getStatus()== UserStatus.BANNED.ordinal()){
            throw new AuthenticationException("您的账号已被管理员禁用");
        }
        String password = user.getPassword();
        String salt = user.getSalt();
        return new SimpleAuthenticationInfo(username,password, ByteSource.Util.bytes(salt),getName());
    }
}
