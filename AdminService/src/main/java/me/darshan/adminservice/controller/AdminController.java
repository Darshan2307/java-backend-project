package me.darshan.adminservice.controller;

import me.darshan.adminservice.dto.ProductDTO;
import me.darshan.adminservice.model.Image;
import me.darshan.adminservice.model.Order;
import me.darshan.adminservice.model.Product;
import me.darshan.adminservice.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = adminService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = adminService.getProductById(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build(); // Product not found
        }
    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody ProductDTO productDTO) {
        Product createdProduct = adminService.createProduct(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        adminService.deleteProduct(id);
        return ResponseEntity.ok("Product with id " + id + " deleted successfully");
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody ProductDTO updatedProduct) {
        Product product = adminService.updateProduct(id, updatedProduct);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build(); // Product not found
        }
    }

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> viewAllOrders() {
        List<Order> orders = adminService.viewAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/images/{imageId}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long imageId) {
        byte[] imageData = adminService.getImageData(imageId);
        if (imageData != null) {
            // Inferring the MIME type from the file extension could be improved
            MediaType mediaType = MediaType.IMAGE_JPEG; // Update as needed
            return ResponseEntity.ok().contentType(mediaType).body(imageData);
        } else {
            return ResponseEntity.notFound().build(); // Image data not found
        }
    }

    @PostMapping("/images/add")
    public ResponseEntity<String> addImage(@RequestBody Map<String, String> requestBody) {
        try {
            String imageName = requestBody.get("imageName");
            if (imageName == null || imageName.isEmpty()) {
                return ResponseEntity.badRequest().body("Image name must not be empty");
            }
            Image image = new Image();
            image.setName(imageName);
            adminService.saveImage(image);
            return ResponseEntity.ok("Image name added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add image name");
        }
    }

    @GetMapping("/images")
    public ResponseEntity<List<Image>> getAllImages() {
        List<Image> images = adminService.getAllImages();
        return ResponseEntity.ok(images);
    }

    @DeleteMapping("/images/{imageId}")
    public ResponseEntity<String> deleteImage(@PathVariable Long imageId) {
        adminService.deleteImage(imageId);
        return ResponseEntity.ok("Image with id " + imageId + " deleted successfully");
    }
}
