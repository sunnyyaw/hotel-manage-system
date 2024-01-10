package com.example.dish.controller;

import com.example.dish.entity.UserDTO;
import com.example.dish.result.Result;
import com.example.dish.services.UserService;
import com.example.dish.utils.StringUtils;
import com.example.dish.utils.VerifyCodeUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

@RestController
@Validated
public class LoginController {
    @Autowired
    private UserService userService;
    @RequestMapping(value="/verifyCode")
    public void getVerifyImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String verifyCode = StringUtils.getRandomString(4);
        BufferedImage image = VerifyCodeUtils.generateVerifyImage(verifyCode);
        request.getSession().setAttribute("verifyCode",verifyCode);
        response.setContentType("image/png");
        OutputStream out = response.getOutputStream();
        ImageIO.write(image,"png",out);
        out.flush();
        out.close();
    }
    @RequestMapping(value="/login",method = RequestMethod.POST)
    public Result<String> login(@Valid @RequestBody UserDTO userForm,
                                HttpSession session){
        if(userForm.getVerifyCode()==null||
                !Objects.equals(session.getAttribute("verifyCode"),
                userForm.getVerifyCode()))
            return new Result<>(400,"验证码错误");
        try{
            userService.login(userForm);
        }catch (AuthenticationException e){
            return new Result<>(401,e.getMessage());
        }
        return new Result<>(200,"登录成功");
    }
    @RequestMapping(value="/register",method = RequestMethod.POST)
    public Result<String> register(@Valid @RequestBody UserDTO user,
                                   HttpSession session){
        if(user.getVerifyCode()==null||
                !Objects.equals(session.getAttribute("verifyCode"),
                        user.getVerifyCode()))
            return new Result<>(400,"验证码错误");
        try{
            userService.register(user);
        } catch (Exception e) {
            return new Result<>(400,e.getMessage());
        }
        return new Result<>(200,"注册成功");
    }
    @RequestMapping(value="/logout",method = RequestMethod.GET)
    public Result<String> logout(){
        userService.logout();
        return new Result<>(200,"登出成功");
    }
    @RequestMapping(value="/authentication",method = RequestMethod.GET)
    public String authentication(){
        return "身份认证成功";
    }
}
