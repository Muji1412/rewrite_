package com.example.rewrite.service.address;

import com.example.rewrite.entity.Address;

import java.util.List;

public interface AddressService {


    public List<Address> getAddress(String uid);
    public void addressWrite(Address address);
    public void checkDefault(Long addressId, String uid);
    public void deleteAddress(Long addressId);
    public Address updateAddress(Long addressId);

}
