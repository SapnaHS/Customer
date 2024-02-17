package com.mobile.banking.Customer.repository;

import com.mobile.banking.Customer.entity.CustomerTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<CustomerTransaction, Long> {
}
