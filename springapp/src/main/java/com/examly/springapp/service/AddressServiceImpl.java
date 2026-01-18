package com.examly.springapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.examly.springapp.model.Address;
import com.examly.springapp.model.User;
import com.examly.springapp.repository.AddressRepo;
import com.examly.springapp.repository.UserRepo;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    AddressRepo addressRepo;

    @Autowired
    UserRepo userRepo;

    @Override
    public Address addAddress(Long userId, Address address) {
        User user = userRepo.findById(userId).orElse(null);

        if(user!=null) {
            address.setUser(user);
            return addressRepo.save(address);
        }

        return null;
    }

    @Override
    public Address updateAddress(Long addressId, Address address) {
        Address existingAddress = addressRepo.findById(addressId).orElse(null);

        if(existingAddress!=null) {
            existingAddress.setAddressData(address.getAddressData());
            existingAddress.setPincode(address.getPincode());
            existingAddress.setCategory(address.getCategory());

            return addressRepo.save(existingAddress);
        }

        return null;
    }

    @Override
    public boolean deleteAddress(Long addressId) {
        Address address = addressRepo.findById(addressId).orElse(null);

        if(address!=null) {
            addressRepo.deleteById(addressId);
            return true;
        }

        return false;
    }
    
}
