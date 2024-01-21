package com.example.dish.controller;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

@RestController
public class CoverController {

    @PostMapping("/covers")
    public String coversUpload(MultipartFile file) throws Exception{
        String originalName = file.getOriginalFilename();
        String suffix = originalName.substring(originalName.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString().replace("-","") + suffix;

        String resourcePath = System.getProperty("user.dir")+"/images/";
        String filePath = resourcePath + fileName;
        File f = new File(filePath);
        if(!f.getParentFile().exists())
            f.mkdirs();
        if(!f.exists())
            f.createNewFile();
        file.transferTo(f);
        return "http://localhost:8443/files/"+fileName;
    }
    @GetMapping("/covers")
    public void getCover(@RequestParam String fileName, HttpServletResponse response)throws Exception{
        File file = new File("images/"+fileName);
        byte[] bytes = new byte[1024];
        int n = 0;
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ServletOutputStream servletOutputStream = response.getOutputStream();
            while(n!=-1){
                n = fileInputStream.read(bytes);
                servletOutputStream.write(bytes,0,n);
            }
            servletOutputStream.flush();
            fileInputStream.close();
            servletOutputStream.close();
            response.setContentType("img/jpeg");
        }catch (FileNotFoundException e){
            throw new Exception("文件不存在");
        }
    }
}
