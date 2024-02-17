package com.mobile.banking.Customer.controller;

import com.mobile.banking.Customer.entity.Customer;
import com.mobile.banking.Customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/getCustomer/{customerId}")
    public List<Customer> getCustomerAccountsInfo(@PathVariable String customerId) {
        return customerService.getCustomerAccountsInfo(customerId);
    }
}
