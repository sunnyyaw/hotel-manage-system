package com.example.dish.interceptor;

import com.example.dish.entity.Permission;
import com.example.dish.entity.User;
import com.example.dish.services.RolePermService;
import com.example.dish.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;
import java.util.regex.Pattern;

public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;
    @Autowired
    private RolePermService rolePermService;
    @Override
    public boolean preHandle (HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,Object o) throws Exception{
        if(HttpMethod.OPTIONS.toString().equals(httpServletRequest.getMethod())){
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            return true;
        }
        Subject subject = SecurityUtils.getSubject();
        if(!subject.isAuthenticated()&&!subject.isRemembered()){
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        if(!rolePermService.matchesByURL(httpServletRequest.getRequestURI()))
            return true;
        User user = userService.getUserByUsername(subject.getPrincipal().toString());
        if(user==null)
            throw new Exception("用户不存在");
        List<Permission> permissions = userService.getPermissionsByUser(user);
        if(permissions.stream().noneMatch(permission->
                Pattern.matches(permission.getUrl(),httpServletRequest.getRequestURI())))
            throw new Exception("您没有访问权限");
        return true;
    }
}
