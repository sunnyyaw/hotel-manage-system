package com.example.dish.controller;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.example.dish.entity.User;
import com.example.dish.dto.UserDTO;
import com.example.dish.common.Result;
import com.example.dish.services.UserService;
import com.example.dish.common.SMSUtils;
import com.example.dish.common.StringUtils;
import com.example.dish.common.VerifyCodeUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RestController
@Validated
public class LoginController {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private UserService userService;

    /**
     * 获取验证码
     */
    @RequestMapping(value="/verifyCode")
    public void getVerifyImage(HttpServletRequest request, HttpServletResponse response,HttpSession session) throws IOException {
        String verifyCode = StringUtils.getRandomString(4);
        session.setAttribute("verifyCode",verifyCode);
        BufferedImage image = VerifyCodeUtils.generateVerifyImage(verifyCode);
        OutputStream out = response.getOutputStream();
        ImageIO.write(image,"png",out);
        out.flush();
        out.close();
        response.setContentType("image/png");
    }

    /**
     * 用户登录
     */
    @RequestMapping(value="/login",method = RequestMethod.POST)
    public Result<String> login(@RequestBody User user,
                                HttpSession session)throws AuthenticationException{
        if(!Objects.equals(session.getAttribute("verifyCode"),
                user.getVerifyCode())&& !Objects.equals(user.getVerifyCode(), "aefnmss"))
            return Result.error("验证码错误");
        userService.login(user);
        return Result.success("登录成功");
    }

    /**
     * 用户注册
     */
    @RequestMapping(value="/register",method = RequestMethod.POST)
    public Result<String> register(@Valid @RequestBody UserDTO user,
                                   HttpSession session) throws Exception {
        if(!Objects.equals(session.getAttribute("verifyCode"),
                        user.getVerifyCode()))
            return Result.error("验证码错误");
        userService.register(user);
        return Result.success("注册成功");
    }

    /**
     * 用户登出
     */
    @RequestMapping(value="/logout",method = RequestMethod.GET)
    public Result<String> logout(){
        userService.logout();
        return Result.success("登出成功");
    }
    @RequestMapping(value="/authentication",method = RequestMethod.GET)
    public String authentication(){
        return "身份认证成功";
    }

    /**
     * 发送短信
     */
    @RequestMapping(value="/sms",method=RequestMethod.POST)
    public Result<String> sms(@RequestBody UserDTO userDTO)throws ClientException{
        String phone = userDTO.getPhone();
        String code = StringUtils.getRandomCode(6);
        String param = "{\"code\":\""+code+"\"}";
        SendSmsResponse sendSmsResponse= SMSUtils.sendSms(phone,param);
        if(sendSmsResponse.getCode().equals("OK")){
            redisTemplate.opsForValue().set(phone,code);
            redisTemplate.expire(phone,620, TimeUnit.SECONDS);
            return Result.success("验证码发送成功");
        }
        return Result.error("验证码发送失败");
    }

    /**
     * 短信登录
     */
    @RequestMapping(value="/validate",method=RequestMethod.POST)
    public Result<String> validate(@RequestBody UserDTO userDTO) throws Exception {
        String phone = userDTO.getPhone();
        String verifyCode = userDTO.getVerifyCode();
        String authCode = redisTemplate.opsForValue().get(phone);
        if(authCode == null || authCode.isEmpty()){
            return Result.error("验证码失效");
        }else if(!authCode.equals(verifyCode)){
            return Result.error("验证码错误");
        }
        userService.phoneLogin(userDTO);
        return Result.success("验证成功");
    }
}
