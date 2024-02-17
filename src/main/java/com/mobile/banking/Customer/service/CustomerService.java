package com.mobile.banking.Customer.service;

import com.mobile.banking.Customer.entity.Customer;
import com.mobile.banking.Customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    public List<Customer> getCustomerAccountsInfo(String customerId) {
        return customerRepository.findByCustomerId(customerId);
    }
}
