package com.ecommerce.admin.repository;

import com.ecommerce.admin.entity.CustomerAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Long> {
    List<CustomerAddress> findByCustomerId(Long customerId);

    @Modifying
    @Query("UPDATE CustomerAddress a SET a.defaultAddress = false WHERE a.customer.id = :customerId")
    void clearDefaultAddresses(@Param("customerId") Long customerId);
}
