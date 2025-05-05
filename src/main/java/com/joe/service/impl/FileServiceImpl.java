package com.joe.service.impl;

import com.joe.exception.EmptyFileException;
import com.joe.exception.FileExistsException;
import com.joe.service.FileService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Transactional
@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Override
    public String uploadFileHandler(String path, MultipartFile file) throws IOException, EmptyFileException, FileExistsException {
        // 1. Check if file has been provided
        if (file.isEmpty()){
            throw new EmptyFileException("File is empty, Please add a file!");
        }
        // 1. Check if the file exists
        if (Files.exists(Paths.get(path + File.separator + file.getOriginalFilename()))){
            throw new FileExistsException("File already exists in that path");
        }
        // Get the name of the file
        String fileName = file.getOriginalFilename();

        // Get the file path
        String filePath = path + File.separator + fileName;

        // Create file object
        File file1 = new File(path);
        if (!file1.exists())
            file1.mkdir();
        // upload file to the path
        Files.copy(file.getInputStream(), Paths.get(filePath));
        return fileName;
    }

    @Override
    public InputStream getResourceFile(String path, String fileName) throws FileNotFoundException {
        String filePath = path + File.separator + fileName;
        return new FileInputStream(filePath);
    }
}
