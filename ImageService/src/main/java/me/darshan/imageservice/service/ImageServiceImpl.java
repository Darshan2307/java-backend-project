package me.darshan.imageservice.service;

import me.darshan.imageservice.model.Image;
import me.darshan.imageservice.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageServiceImpl implements ImageService {
    //changed the file path for images
    private static final String IMAGE_DIRECTORY = "C:/Users/drath/Desktop/images/";

    @Autowired
    private ImageRepository imageRepository;

    @Override
    public byte[] getImageData(Long imageId) {
        Image image = imageRepository.findById(imageId).orElse(null);
        if (image != null) {
            try {
                // Construct the image file path using the image name from the entity
                Path imagePath = Paths.get(IMAGE_DIRECTORY + image.getName());
                System.out.println("hello");
                return Files.readAllBytes(imagePath);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public void saveImage(Image image) {
        imageRepository.save(image);
    }
}
