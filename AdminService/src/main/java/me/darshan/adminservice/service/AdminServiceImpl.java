package me.darshan.adminservice.service;

import jakarta.transaction.Transactional;

import me.darshan.adminservice.dto.ProductDTO;
import me.darshan.adminservice.exceptions.ImageNotFoundException;
import me.darshan.adminservice.exceptions.ProductNotFoundException;
import me.darshan.adminservice.model.Image;
import me.darshan.adminservice.model.Order;
import me.darshan.adminservice.model.Product;
import me.darshan.adminservice.repository.ImageRepository;
import me.darshan.adminservice.repository.OrderRepository;
import me.darshan.adminservice.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    // Assuming images are stored in a directory named "images" within the project directory
    private static final String IMAGE_DIRECTORY = "C:/Users/drath/Desktop/images/"; // Update with your images directory's absolute path

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    private final ImageRepository imageRepository;

    public AdminServiceImpl(OrderRepository orderRepository, ProductRepository productRepository, ImageRepository imageRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.imageRepository = imageRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() ->
                new ProductNotFoundException("Product with id " + id + " not found"));
    }

    @Override
    @Transactional
    public Product createProduct(ProductDTO productDTO) {
        String imageIdString = productDTO.getImageId();
        Image image = imageRepository.findById(Long.parseLong(imageIdString))
                .orElseThrow(() -> new ImageNotFoundException("Image with id " + imageIdString + " not found"));

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());
        product.setImage(image);

        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product with id " + id + " not found");
        }
        productRepository.deleteById(id);
    }

    @Override
    public Product updateProduct(Long id, ProductDTO updatedProductDTO) {
        Product existingProduct = productRepository.findById(id).orElseThrow(() ->
                new ProductNotFoundException("Product with id " + id + " not found"));

        existingProduct.setName(updatedProductDTO.getName());
        existingProduct.setDescription(updatedProductDTO.getDescription());
        existingProduct.setPrice(updatedProductDTO.getPrice());
        existingProduct.setStock(updatedProductDTO.getStock());

        // Check if the updated product DTO has a non-null image ID
        if (updatedProductDTO.getImageId() != null) {
            String imageIdString = updatedProductDTO.getImageId();
            Image image = imageRepository.findById(Long.parseLong(imageIdString))
                    .orElseThrow(() -> new ImageNotFoundException("Image with id " + imageIdString + " not found"));
            existingProduct.setImage(image);
        }

        return productRepository.save(existingProduct);
    }

    @Override
    public List<Order> viewAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public byte[] getImageData(Long imageId) {
        Image image = imageRepository.findById(imageId).orElse(null);
        if (image != null) {
            try {
                // Construct the image file path using the image name from the entity
                Path imagePath = Paths.get(IMAGE_DIRECTORY + image.getName());
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

    @Override
    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    @Override
    public void deleteImage(Long imageId) {
        if (!imageRepository.existsById(imageId)) {
            throw new ImageNotFoundException("Image with id " + imageId + " not found");
        }
        imageRepository.deleteById(imageId);
    }
}