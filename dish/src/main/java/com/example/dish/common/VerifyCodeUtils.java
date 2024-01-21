package com.example.dish.common;


import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class VerifyCodeUtils {
    private static final int height = 100;
    private static final int width = 200;
    public static BufferedImage generateVerifyImage(String verifyCode){
        BufferedImage bufferedImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
        graphics.setColor(Color.white);
        graphics.fillRect(0,0,width,height);
        graphics.setFont(new Font("微软雅黑",Font.BOLD,20));
        graphics.setColor(Color.BLACK);
        char[] chars = verifyCode.toCharArray();
        int x = 20;
        Random random = new Random();
        for(int i=0;i<4 && i<chars.length;i++){
            double degree = random.nextDouble(Math.PI/6);
            graphics.rotate(degree,x,40);
            graphics.drawString(String.valueOf(chars[i]),x,40);
            graphics.rotate(-degree,x,40);
            x += 40;
        }
        for(int i=0;i<6;i++){
            int r = random.nextInt(256);
            int g = random.nextInt(256);
            int b = random.nextInt(256);
            graphics.setColor(new Color(r,g,b));
            graphics.drawLine(random.nextInt(width),random.nextInt(height),
                    random.nextInt(width),random.nextInt(height));
        }
        for(int i=0;i<30;i++){
            int r = random.nextInt(256);
            int g = random.nextInt(256);
            int b = random.nextInt(256);
            graphics.setColor(new Color(r,g,b));
            graphics.fillOval(random.nextInt(width), random.nextInt(height),
                    4,4);
        }
        return bufferedImage;
    }
}
