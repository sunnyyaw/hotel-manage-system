package com.example.dish.controller;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.example.dish.entity.UserDTO;
import com.example.dish.result.Result;
import com.example.dish.services.UserService;
import com.example.dish.utils.SMSUtils;
import com.example.dish.utils.StringUtils;
import com.example.dish.utils.VerifyCodeUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RestController
@Validated
public class LoginController {
    @Autowired
    private StringRedisTemplate redisTemplate;
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
    @RequestMapping(value="/sms",method=RequestMethod.POST)
    public Map<String,Object> sms(@RequestBody Map<String,Object> requestMap,HttpServletRequest request)throws ClientException{
        Map<String,Object> map = new HashMap<>();
        String phone = requestMap.get("phoneNumber").toString();
        String code = StringUtils.getRandomCode(6);
        String param = "{\"code\":\""+code+"\"}";
        SendSmsResponse sendSmsResponse= SMSUtils.sendSms(phone,param);
        map.put("phone",phone);
        map.put("verifyCode",code);
        request.getSession().setAttribute("CodePhone",map);
        if(sendSmsResponse.getCode().equals("OK")){
            map.put("isOk","OK");
            redisTemplate.opsForValue().set(phone,code);
            redisTemplate.expire(phone,620, TimeUnit.SECONDS);
        }
        return map;
    }
    @RequestMapping(value="/validate",method=RequestMethod.POST)
    public Result<String> validate(@RequestBody Map<String,Object> requestMap) throws ClientException{
        Map<String,Object> map = new HashMap<>();
        String phone = requestMap.get("phone").toString();
        String verifyCode = requestMap.get("verifyCode").toString();
        String authCode = redisTemplate.opsForValue().get(phone);
        if(authCode == null || authCode.isEmpty()){
            return new Result<>(404,"验证码失效");
        }else if(!authCode.equals(verifyCode)){
            return new Result<>(500,"验证码错误");
        }
        return new Result<>(200,"验证成功");

    }
}
