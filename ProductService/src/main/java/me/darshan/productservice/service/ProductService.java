package me.darshan.productservice.service;


import me.darshan.productservice.dto.ProductDTO;
import me.darshan.productservice.model.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();

    Product getProductById(Long id);

    Product createProduct(ProductDTO productDTO);

    Product updateProduct(Long id, ProductDTO updatedProductDTO);

    void deleteProduct(Long id);
}
