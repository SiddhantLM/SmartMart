package com.examly.springapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.examly.springapp.model.Address;
import com.examly.springapp.service.AddressService;

@RestController
public class AddressController {
    @Autowired
    AddressService service;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/api/address/user/{userId}")
    public ResponseEntity<Address> addAddress (@PathVariable Long userId, @RequestBody Address address) {
        try {
            Address newAddress = service.addAddress(userId, address);
            if(newAddress != null) {
                return new ResponseEntity<>(newAddress, HttpStatusCode.valueOf(201));
            } else {
                return new ResponseEntity<>( HttpStatusCode.valueOf(500));
            }         
        } catch (Exception e) {
            return new ResponseEntity<>( HttpStatusCode.valueOf(500));
        }
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/api/address/{addressId}")
    public ResponseEntity<Address> updateAddress (@PathVariable Long addressId, @RequestBody Address address) {
        try {
            Address newAddress = service.updateAddress(addressId, address);
            if(newAddress != null) {
                return new ResponseEntity<>(newAddress, HttpStatusCode.valueOf(201));
            } else {
                return new ResponseEntity<>( HttpStatusCode.valueOf(404));
            }         
        } catch (Exception e) {
            return new ResponseEntity<>( HttpStatusCode.valueOf(500));
        }
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/api/address/{addressId}")
    public ResponseEntity<Boolean> deleteAddress (@PathVariable Long addressId) {
        try {
            boolean newAddress = service.deleteAddress(addressId);
            if(newAddress) {
                return new ResponseEntity<>(newAddress, HttpStatusCode.valueOf(201));
            } else {
                return new ResponseEntity<>( newAddress ,HttpStatusCode.valueOf(404));
            }         
        } catch (Exception e) {
            return new ResponseEntity<>( HttpStatusCode.valueOf(500));
        }
    }
}
