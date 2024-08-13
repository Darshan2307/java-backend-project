package me.darshan.adminservice.service;



import me.darshan.adminservice.dto.ProductDTO;
import me.darshan.adminservice.model.Image;
import me.darshan.adminservice.model.Order;
import me.darshan.adminservice.model.Product;

import java.util.List;

public interface AdminService {
    List<Product> getAllProducts();

    Product getProductById(Long id);

    Product createProduct(ProductDTO productDTO);

    void deleteProduct(Long id);

    Product updateProduct(Long id, ProductDTO updatedProductDTO);

    List<Order> viewAllOrders();

    byte[] getImageData(Long imageId);

    void saveImage(Image image);

    List<Image> getAllImages();

    void deleteImage(Long imageId);
}
