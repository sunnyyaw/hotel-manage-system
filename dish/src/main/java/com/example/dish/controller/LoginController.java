package com.example.dish.controller;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.example.dish.entity.UserDTO;
import com.example.dish.common.Result;
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
     * @param request
     * @param response
     * @throws IOException
     */
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

    /**
     * 用户登录
     * @param userForm
     * @param session
     * @return
     */
    @RequestMapping(value="/login",method = RequestMethod.POST)
    public Result<String> login(@Valid @RequestBody UserDTO userForm,
                                HttpSession session){
        if(!Objects.equals(session.getAttribute("verifyCode"),
                userForm.getVerifyCode()))
            return new Result<>(400,"验证码错误");
        try{
            userService.login(userForm);
        }catch (AuthenticationException e){
            return new Result<>(401,e.getMessage());
        }
        return new Result<>(200,"登录成功");
    }

    /**
     * 用户注册
     * @param user
     * @param session
     * @return
     */
    @RequestMapping(value="/register",method = RequestMethod.POST)
    public Result<String> register(@Valid @RequestBody UserDTO user,
                                   HttpSession session){
        if(!Objects.equals(session.getAttribute("verifyCode"),
                        user.getVerifyCode()))
            return new Result<>(400,"验证码错误");
        try{
            userService.register(user);
        } catch (Exception e) {
            return new Result<>(400,e.getMessage());
        }
        return new Result<>(200,"注册成功");
    }

    /**
     * 用户登出
     * @return
     */
    @RequestMapping(value="/logout",method = RequestMethod.GET)
    public Result<String> logout(){
        userService.logout();
        return new Result<>(200,"登出成功");
    }
    @RequestMapping(value="/authentication",method = RequestMethod.GET)
    public String authentication(){
        return "身份认证成功";
    }

    /**
     * 发送短信
     * @param userDTO
     * @return
     * @throws ClientException
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
            return new Result<>(200,"验证码发送成功");
        }
        return new Result<>(500,"验证码发送失败");
    }

    /**
     * 短信登录
     * @param userDTO
     * @return
     */
    @RequestMapping(value="/validate",method=RequestMethod.POST)
    public Result<String> validate(@RequestBody UserDTO userDTO){
        String phone = userDTO.getPhone();
        String verifyCode = userDTO.getVerifyCode();
        String authCode = redisTemplate.opsForValue().get(phone);
        if(authCode == null || authCode.isEmpty()){
            return new Result<>(404,"验证码失效");
        }else if(!authCode.equals(verifyCode)){
            return new Result<>(500,"验证码错误");
        }
        try{
            userService.phoneLogin(userDTO);
        }catch (Exception e){
            return new Result<>(500,"验证失败");
        }
        return new Result<>(200,"验证成功");
    }
}
