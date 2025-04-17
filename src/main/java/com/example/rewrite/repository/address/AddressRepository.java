package com.example.rewrite.repository.address;

import com.example.rewrite.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address,Long> {
}
