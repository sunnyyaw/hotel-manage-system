package com.example.dish.realm;

import com.example.dish.entity.Permission;
import com.example.dish.entity.Role;
import com.example.dish.entity.User;
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

import java.util.List;
import java.util.Objects;

public class UserPasswordRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return new SimpleAuthorizationInfo();
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username = authenticationToken.getPrincipal().toString();
        User user = userService.getUserByUsername(username);
        if(Objects.isNull(user)){
            throw new AuthenticationException("用户不存在");
        }
        String password = user.getPassword();
        String salt = user.getSalt();
        return new SimpleAuthenticationInfo(username,password, ByteSource.Util.bytes(salt),getName());
    }
}
