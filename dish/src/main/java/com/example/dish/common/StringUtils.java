package com.example.dish.common;
import java.util.Random;
public class StringUtils {
    public static String getRandomString(int length){
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<length;i++){
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
    public static String getRandomCode(int length){
        String base = "0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<length;i++){
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}
