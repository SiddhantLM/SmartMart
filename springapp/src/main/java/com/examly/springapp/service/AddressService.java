package com.examly.springapp.service;

import com.examly.springapp.model.Address;

public interface AddressService {
    Address addAddress (Long userId, Address address);
    Address updateAddress(Long addressId, Address address);
    boolean deleteAddress (Long addressId);
}
