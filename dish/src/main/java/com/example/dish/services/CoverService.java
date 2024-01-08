package com.example.dish.services;

import org.springframework.web.multipart.MultipartFile;

public interface CoverService {
    String coversUpload(MultipartFile file) throws Exception;
}
