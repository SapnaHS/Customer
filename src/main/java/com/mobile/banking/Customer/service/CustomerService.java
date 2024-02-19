package com.mobile.banking.Customer.service;

import com.mobile.banking.Customer.entity.Customer;
import com.mobile.banking.Customer.exception.OperationNotAllowedException;
import com.mobile.banking.Customer.repository.CustomerRepository;
import com.mobile.banking.Customer.utility.CustomerUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> getCustomerAccountsInfo(String customerId) {

        try {
            if (!CustomerUtility.getCustomerIdFromToken().equals(customerId))
                throw new OperationNotAllowedException("You cannot access this account");
            return customerRepository.findByCustomerId(customerId);

        } catch (OperationNotAllowedException e) {
            throw new RuntimeException(e);
        }
    }

}
