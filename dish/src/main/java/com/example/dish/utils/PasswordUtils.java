package com.example.dish.utils;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;

public class PasswordUtils {
    public static String encodePassword(String password,String salt){
        return new SimpleHash("md5",password,salt,2).toString();
    }
    public static String generateSalt(){
        return new SecureRandomNumberGenerator().nextBytes().toString();
    }
}
