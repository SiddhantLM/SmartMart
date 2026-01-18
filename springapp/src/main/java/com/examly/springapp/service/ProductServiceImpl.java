package com.examly.springapp.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.examly.springapp.model.Product;
import com.examly.springapp.model.ProductRequest;
import com.examly.springapp.model.Review;
import com.examly.springapp.model.User;
import com.examly.springapp.repository.ProductRepo;
import com.examly.springapp.repository.ReviewRepo;
import com.examly.springapp.repository.UserRepo;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepo productRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    ReviewRepo reviewRepo;

    @Override
    public Product addProduct(Product product) {
        product.setCreatedAt(LocalDate.now());
        product.setUpdatedAt(LocalDate.now());
        return productRepo.save(product);
    }

    @Override
    public List<Product> getProducts() {
        return productRepo.findAll();
    }

    @Override
    public Product getProductsById(Long id) {
        return productRepo.findById(id).orElse(null);
    }

    @Override
    public List<Product> getProductsByUserId(Long userId) {
       return productRepo.findByUserUserId(userId);
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepo.findByCategory(category);
    }

    @Override
    public boolean deleteProduct(Long id) {
       Product p=productRepo.findById(id).orElse(null);
       if(p!=null){
        productRepo.deleteById(id);
        return true;
       }
       return false;
    }

    @Override
    public Product updateProduct(Long productId, ProductRequest productRequest) {
       Product p=productRepo.findById(productId).orElse(null);
       if(p!=null){
        p.setName(productRequest.getName());
        p.setDescription(productRequest.getDescription());
        p.setPrice(productRequest.getPrice());
        p.setStock(productRequest.getStock());
        p.setCategory(productRequest.getCategory());
        if(productRequest.getBase64Image()!=null && !productRequest.getBase64Image().isEmpty()){
            p.setPhotoImage(productRequest.getBase64Image());
        }
        p.setUpdatedAt(LocalDate.now());
        return productRepo.save(p);
       }
       return null;
    }
   
    @Override
    public Review addReview (Long productId, Long userId ,Review review) {
        Product product = productRepo.findById(productId).orElse(null);
        User user = userRepo.findById(userId).orElse(null);
        if(product!=null && user!=null) {
            review.setProduct(product);
            review.setUser(user);
            product.getReviews().add(review);

            product.setRating((product.getRating()+review.getRating())/product.getReviews().size());

            return reviewRepo.save(review);
        }

        return null;
    }

    @Override
    public Product toggleWishlist(Long productId, Long userId) {
        User user = userRepo.findById(userId).orElse(null);
        Product product = productRepo.findById(productId).orElse(null);

        if(user!=null && product!=null) {
            if(user.getWishlist().contains(product)) {
                List<Product> newWishlist = user.getWishlist();
                newWishlist.remove(newWishlist.indexOf(product));
                user.setWishlist(newWishlist);
            } else {
                List<Product> newWishlist = user.getWishlist();
                newWishlist.add(product);
                user.setWishlist(newWishlist);
            }
            userRepo.save(user); //saved the chnges in user
        }

        return null;
    }

    @Override
    public List<Product> getUserWishlist(Long userId) {
       User user= userRepo.findById(userId).orElse(null);
       if(user!=null){
        return user.getWishlist();
       }
       return new ArrayList<>();
    }

    
    
}
