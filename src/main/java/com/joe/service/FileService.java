package com.joe.service;

import com.joe.exception.FileExistsException;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface FileService {
    String uploadFileHandler(String path, MultipartFile file) throws IOException, FileExistsException;

    InputStream getResourceFile(String path, String fileName) throws FileNotFoundException;
}
