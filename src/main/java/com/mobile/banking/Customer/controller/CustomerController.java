package com.mobile.banking.Customer.controller;

import com.mobile.banking.Customer.dto.AuthDTO;
import com.mobile.banking.Customer.entity.Customer;
import com.mobile.banking.Customer.service.CustomerService;
import com.mobile.banking.Customer.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/customer/{customerId}")
    public List<Customer> getCustomerAccountsInfo(@PathVariable String customerId) {
        return customerService.getCustomerAccountsInfo(customerId);
    }

    @PostMapping("/authenticate")
    public String authenticateUser(@RequestBody AuthDTO authDTO) {
        return jwtService.generateToken(authDTO.getCustomerId());
    }
}
