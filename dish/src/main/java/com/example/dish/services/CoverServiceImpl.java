package com.example.dish.services;
import com.example.dish.config.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class CoverServiceImpl implements CoverService{
    private final Path rootLocation;
    @Autowired
    public CoverServiceImpl(StorageProperties storageProperties){
        this.rootLocation = Paths.get(storageProperties.getLocation());
    }
    @Override
    public String coversUpload(MultipartFile file) throws Exception{
        String filePath = UUID.randomUUID().toString().replace("-","") +
                file.getOriginalFilename().substring(file.getOriginalFilename().length()-4);
        Path destinationFile = this.rootLocation.resolve(Paths.get(filePath)).toAbsolutePath();

        if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())){
            throw new Exception("cannot store file outside current directory");
        }
        try {
            File f = new File(destinationFile.toString());
            if(!f.exists())
                f.createNewFile();
            file.transferTo(f);
            return "http://localhost:8443/files/" + filePath;
        } catch (IOException e){
            e.printStackTrace();
            return "";
        }
    }
}
