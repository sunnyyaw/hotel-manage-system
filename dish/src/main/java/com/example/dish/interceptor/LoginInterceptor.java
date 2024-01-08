package com.example.dish.interceptor;

import com.example.dish.entity.Permission;
import com.example.dish.entity.User;
import com.example.dish.utils.PermissionUtils;
import com.example.dish.utils.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private PermissionUtils permissionUtils;
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
        String url = httpServletRequest.getRequestURI();
        if(!permissionUtils.anyMatchesURL(url))
            return true;
        User user = userUtils.getUserByUsername(subject.getPrincipal().toString());
        if(Objects.isNull(user))
            throw new Exception("找不到用户");
        List<Permission> permissions = userUtils.getPermissionsByUser(user);
        if(permissions.stream().anyMatch(permission -> Pattern.matches(permission.getUrl(),url)))
            return true;
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        return false;
    }
}
