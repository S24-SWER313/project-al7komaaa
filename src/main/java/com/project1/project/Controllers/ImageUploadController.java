package com.project1.project.Controllers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


public class ImageUploadController {


    public ImageUploadController() {
    }



    private String VIDEO_UPLOAD_DIR = "";
    private static final String IMAGE_UPLOAD_DIR = "";


   
    public String uploadImage( MultipartFile file)  {
        if (file.isEmpty()) {
            return "Please select a file to upload";
        }
    Path path=null;
        try { 
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
             path = Paths.get(IMAGE_UPLOAD_DIR + fileName);
            Files.copy(file.getInputStream(), path);
            return fileName ;
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to upload image. File: " ;

        }
    }


    public String uploadVideo(MultipartFile file) {
        if (file.isEmpty()) {
            return null;
        }
        
        Path path = null;
        try {
            // Generate a unique file name for the video
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            path = Paths.get(VIDEO_UPLOAD_DIR + fileName);
            
            // Save the video file to the specified directory
            Files.copy(file.getInputStream(), path);
            
            // Return the generated file name
            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
   
    
}    
