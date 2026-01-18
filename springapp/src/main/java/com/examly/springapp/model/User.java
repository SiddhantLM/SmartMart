package com.examly.springapp.model;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "userId")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String email;
    private String password;
    private String username;
    private String mobileNumber;
 
    @Enumerated(EnumType.STRING)
    private UserRole userRole;
 
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();
 
    @OneToMany (mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Product> products = new ArrayList<>();
 
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cartId")
    @JsonManagedReference
    private Cart cart;
 
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Address> addresses = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "wishlist")
    private List<Product> wishlist = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnore  // Add this line
    private List<Feedback> feedbacks;
 

 
    public User(Long userId, String email, String password, String username, String mobileNumber, UserRole userRole,
            List<Order> orders, List<Product> products, Cart cart, List<Address> addresses) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.username = username;
        this.mobileNumber = mobileNumber;
        this.userRole = userRole;
        this.orders = orders;
        this.products = products;
        this.cart = cart;
        this.addresses = addresses;
    }
 
 
 
    public User() {
    }
 
    public Cart getCart() {
        return cart;
    }
 
    public void setCart(Cart cart) {
        this.cart = cart;
    }
 
    public List<Address> getAddresses() {
        return addresses;
    }
 
    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }
 
    public Long getUserId() {
        return userId;
    }
 
    public void setUserId(Long userId) {
        this.userId = userId;
    }
 
    public String getEmail() {
        return email;
    }
 
    public List<Order> getOrders() {
        return orders;
    }
 
    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
 
    public List<Product> getProducts() {
        return products;
    }
 
    public void setProducts(List<Product> products) {
        this.products = products;
    }
 
    public void setEmail(String email) {
        this.email = email;
    }
 
    public String getPassword() {
        return password;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }
 
    public String getUsername() {
        return username;
    }
 
    public void setUsername(String username) {
        this.username = username;
    }
 
    public String getMobileNumber() {
        return mobileNumber;
    }
 
    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
 
    public UserRole getUserRole() {
        return userRole;
    }
 
    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
 

    

    @Override
    public String toString() {
        return "User [userId=" + userId + ", email=" + email + ", password=" + password + ", username=" + username
                + ", mobileNumber=" + mobileNumber + ", userRole=" + userRole + "]";
    }

    public List<Product> getWishlist() {
        return wishlist;
    }

    public void setWishlist(List<Product> wishlist) {
        this.wishlist = wishlist;
    }
}