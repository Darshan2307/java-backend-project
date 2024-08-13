package me.darshan.productservice.service;

import jakarta.transaction.Transactional;

import me.darshan.productservice.dto.ProductDTO;
import me.darshan.productservice.exceptions.ImageNotFoundException;
import me.darshan.productservice.exceptions.ProductNotFoundException;
import me.darshan.productservice.model.Image;
import me.darshan.productservice.model.Product;
import me.darshan.productservice.repository.ImageRepository;
import me.darshan.productservice.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ImageRepository imageRepository;
    private final ProductRepository productRepository;

    public ProductServiceImpl(ImageRepository imageRepository, ProductRepository productRepository) {
        this.imageRepository = imageRepository;
        this.productRepository = productRepository;
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
}
