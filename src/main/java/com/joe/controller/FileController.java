package com.joe.controller;

import com.joe.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
@Tag(name = "File Controller", description = "File management endpoints")
public class FileController {
    private final FileService fileService;

    @Value("${project.poster}")
    public String path;

    @PostMapping("/v1/upload-file")
    @Operation(summary = "Adds the movie poster", description = "Uploads the movie poster")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved poster"),
            @ApiResponse(responseCode = "401", description = "Not authorized to view poster"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<String> uploadFile(@RequestPart MultipartFile file) throws IOException {
        String fileName = fileService.uploadFileHandler(path, file);
        return ResponseEntity.ok(file.getOriginalFilename() + " Uploaded successfully");
    }

    @GetMapping(value = "/v1/get-file/{fileName}")
    @Operation(summary = "Get movie poster", description = "Retrieves the movie poster")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved poster"),
            @ApiResponse(responseCode = "401", description = "Not authorized to view poster"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public void getUploadedFile(@PathVariable String fileName, HttpServletResponse response)
            throws IOException {
        InputStream fileResource = fileService.getResourceFile(path, fileName);
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        StreamUtils.copy(fileResource, response.getOutputStream());
    }
}
