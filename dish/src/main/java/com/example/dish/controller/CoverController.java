package com.example.dish.controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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
        return "http://localhost:8443/files/" + fileName;
    }
}
