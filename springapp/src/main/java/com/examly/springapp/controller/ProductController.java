package com.examly.springapp.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.examly.springapp.model.Product;
import com.examly.springapp.model.ProductRequest;
import com.examly.springapp.model.Review;
import com.examly.springapp.service.AiReviewSummaryServiceImpl;
import com.examly.springapp.service.ProductService;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private AiReviewSummaryServiceImpl aiService;

   
    @PostMapping("/products")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addProduct(@RequestBody ProductRequest productRequest) {
        try {
            Product product = new Product();
            product.setName(productRequest.getName());
            product.setDescription(productRequest.getDescription());
            product.setPrice(productRequest.getPrice());
            product.setStock(productRequest.getStock());
            product.setCategory(productRequest.getCategory());
            product.setPhotoImage(productRequest.getBase64Image());
            // product.setUser(productRequest.getUser());

            Product saved = productService.addProduct(product);
            return new ResponseEntity<>(saved, HttpStatusCode.valueOf(201));
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            // conflicts (e.g., unique constraints)
            return new ResponseEntity<>("Conflict while creating product", HttpStatusCode.valueOf(409));
        } catch (Exception e) {
            // treat other add failures as conflict per spec
            return new ResponseEntity<>("Unable to create product", HttpStatusCode.valueOf(409));
        }
    }


    @PutMapping("/products/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
        try {
            Product updated = productService.updateProduct(id, productRequest);
            if (updated != null) {
                return new ResponseEntity<>(updated, HttpStatusCode.valueOf(200));
            } else {
                return new ResponseEntity<>("Product not found", HttpStatusCode.valueOf(404));
            }
        } catch (Exception e) {
            // If authorization fails, Spring Security will return 403 automatically;
            // this catch is just to be consistent with your reference style.
            return new ResponseEntity<>("Access Denied or Error occurred", HttpStatusCode.valueOf(500));
        }
    }


    @GetMapping("/products/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        try {
            Product product = productService.getProductsById(id);
            if (product != null) {
                return new ResponseEntity<>(product, HttpStatusCode.valueOf(200));
            } else {
                return new ResponseEntity<>("Product not found", HttpStatusCode.valueOf(404));
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Access Denied or Error occurred", HttpStatusCode.valueOf(500));
        }
    }


    @DeleteMapping("/product/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            boolean deleted = productService.deleteProduct(id);
            if (deleted) {
                return new ResponseEntity<>("Product deleted successfully", HttpStatusCode.valueOf(200));
            } else {
                return new ResponseEntity<>("Product not found", HttpStatusCode.valueOf(404));
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Access Denied or Error occurred", HttpStatusCode.valueOf(500));
        }
    }


    @GetMapping("/products/category/{category}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> getProductsByCategory(@PathVariable String category) {
        try {
            List<Product> products = productService.getProductsByCategory(category);
            if (products != null && !products.isEmpty()) {
                return new ResponseEntity<>(products, HttpStatusCode.valueOf(200));
            } else {
                return new ResponseEntity<>("No products found for category", HttpStatusCode.valueOf(404));
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Access Denied or Error occurred", HttpStatusCode.valueOf(500));
        }
    }


    @GetMapping("/products")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> getAllProducts() {
        try {
            System.out.println("here");
            List<Product> products = productService.getProducts();
            if (products != null && !products.isEmpty()) {
                return new ResponseEntity<>(products, HttpStatusCode.valueOf(200));
            } else {
                // per your requirement: 400 when list is empty
                return new ResponseEntity<>("No products available", HttpStatusCode.valueOf(400));
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Access Denied or Error occurred", HttpStatusCode.valueOf(500));
        }
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/products/{productId}/user/{userId}")
    public ResponseEntity<Review> addReview (@PathVariable Long productId, @PathVariable Long userId, @RequestBody Review review){
        try {
            return new ResponseEntity<>(productService.addReview(productId, userId, review), HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            return new ResponseEntity<>( HttpStatusCode.valueOf(500));
        }
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/products/{productId}/wishlist/user/{userId}")
    public ResponseEntity<Boolean> toggleWishlist (@PathVariable Long productId, @PathVariable Long userId) {
        try {
            productService.toggleWishlist(productId, userId);
            return new ResponseEntity<>(true, HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatusCode.valueOf(500));
        }
    }


    @GetMapping("/user/{userId}/wishlist")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Product>> getUserWishlist(@PathVariable Long userId){
        try{
            List<Product> wishlist=productService.getUserWishlist(userId);
            return new ResponseEntity<>(wishlist,HttpStatusCode.valueOf(200));
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatusCode.valueOf(500));
        }
    }


    @PostMapping("/products/summary")
    public ResponseEntity<Map<String,String>> summarizedReviews(@RequestBody List<Review> reviews){
        try {
            String summary=aiService.generateSummary(reviews);
            return ResponseEntity.ok(Map.of("summary", summary));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("summary", "Sorry, AI summarizer is temporarily unavailable."));
        }
    }
}
