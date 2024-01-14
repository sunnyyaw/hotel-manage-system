package com.example.dish.services;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class CoverServiceImpl implements CoverService{
    @Override
    public String coversUpload(MultipartFile file) throws Exception{
        String fileName = UUID.randomUUID().toString().replace("-","") +
                file.getOriginalFilename().substring(file.getOriginalFilename().length()-4);
        try {
            String resourcePath = this.getClass().getResource("").getPath()+"images/";
            String filePath = URLDecoder.decode(resourcePath, StandardCharsets.UTF_8) + fileName;
            File f = new File(filePath);
            if(!f.getParentFile().exists())
                f.mkdirs();
            if(!f.exists())
                f.createNewFile();
            file.transferTo(f);
            return "http://localhost:8443/files/" + fileName;
        } catch (IOException e){
            e.printStackTrace();
            return "";
        }
    }
}
