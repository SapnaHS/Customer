package com.mobile.banking.Customer.repository;

import com.mobile.banking.Customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByCustomerId(String customerId);

    Optional<Customer> findByAccountNumber(String accountNumber);
}
