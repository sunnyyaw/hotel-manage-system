package com.example.dish.common;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;

public class SMSUtils {
    static final String product = "Dysmaapi";
    static final String domain = "dysmsapi.aliyuncs.com";
    static final String accessKeyId = "";
    static final String accessKeySecret = "";
    public static SendSmsResponse sendSms(String phone, String code) throws ClientException{
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou",accessKeyId,accessKeySecret);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phone);
        request.setSignName("阿里云短信测试");
        request.setTemplateCode("SMS_154950909");
        request.setTemplateParam(code);
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        return sendSmsResponse;
    }
}
