package com.mobile.banking.Customer.service;

import com.mobile.banking.Customer.dto.CustomerCredentialDetails;
import com.mobile.banking.Customer.entity.CustomerCredential;
import com.mobile.banking.Customer.repository.CustomerCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomerCredentialService implements UserDetailsService {

    @Autowired
    private CustomerCredentialRepository customerCredentialRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<CustomerCredential> credential = customerCredentialRepository.findByCustomerId(username);
        return credential.map(CustomerCredentialDetails::new).orElseThrow(() ->new UsernameNotFoundException("User Not Found "+ username));
    }
}
