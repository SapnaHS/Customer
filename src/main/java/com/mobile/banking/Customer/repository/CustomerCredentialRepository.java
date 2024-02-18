package com.mobile.banking.Customer.repository;

import com.mobile.banking.Customer.entity.CustomerCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerCredentialRepository extends JpaRepository<CustomerCredential, Long> {
    Optional<CustomerCredential> findByCustomerId(String username);
}
