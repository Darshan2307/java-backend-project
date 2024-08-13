package me.darshan.imageservice.service;


import me.darshan.imageservice.model.Image;

public interface ImageService {
    byte[] getImageData(Long imageId);

    void saveImage(Image image);
}
