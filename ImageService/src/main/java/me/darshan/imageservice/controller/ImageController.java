package me.darshan.imageservice.controller;


import me.darshan.imageservice.model.Image;
import me.darshan.imageservice.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/images")
public class ImageController {
    @Autowired
    private ImageService imageService;

    @GetMapping("/{imageId}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long imageId) {
        byte[] imageData = imageService.getImageData(imageId);
        if (imageData != null) {
            // Infer the MIME type based on the file extension
            // Assuming all images are in JPEG format for simplicity
            MediaType mediaType = MediaType.IMAGE_JPEG;
            return ResponseEntity.ok().contentType(mediaType).body(imageData);
        } else {
            return ResponseEntity.notFound().build(); // Image data not found
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addImage(@RequestBody Map<String, String> requestBody) {
        try {
            // Extract the image name from the JSON data
            String imageName = requestBody.get("imageName");

            // Create a new Image object with the provided image name
            Image image = new Image();
            image.setName(imageName);

            // Save the image name to the database
            imageService.saveImage(image);

            return new ResponseEntity<>("Image name added successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to add image name", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}