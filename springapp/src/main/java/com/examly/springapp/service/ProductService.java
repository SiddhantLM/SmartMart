package com.examly.springapp.service;

import java.util.List;

import com.examly.springapp.model.Product;
import com.examly.springapp.model.ProductRequest;
import com.examly.springapp.model.Review;

public interface ProductService {
    Product addProduct(Product product);

    List<Product> getProducts();

    Product getProductsById(Long id);

    List<Product> getProductsByCategory(String category);

    boolean deleteProduct(Long id);

    Product updateProduct(Long productId, ProductRequest productRequest);

    Review addReview(Long productId, Long userId, Review review);

    Product toggleWishlist(Long productId, Long userId);

    List<Product> getUserWishlist(Long userId);

}
