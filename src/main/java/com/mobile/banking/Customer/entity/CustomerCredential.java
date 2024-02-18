package com.mobile.banking.Customer.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "CustomerCredential")
public class CustomerCredential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    String customerId;
    String password;
}
