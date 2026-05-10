package com.happyshop.service;

import com.happyshop.entity.Address;
import java.util.List;

public interface AddressService {

    List<Address> getAddresses(Long userId);

    void addAddress(Long userId, Address address);

    void updateAddress(Long userId, Long addressId, Address address);

    void deleteAddress(Long userId, Long addressId);

    void setDefault(Long userId, Long addressId);
}
