package com.example.rewrite.service.address;


import com.example.rewrite.entity.Address;
import com.example.rewrite.repository.address.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService{

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public List<Address> getAddress() {
        return addressRepository.findAll();
    }
}
