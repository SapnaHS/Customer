package com.mobile.banking.Customer.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "CustomerTransaction")
@Data
public class CustomerTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;
    private String transactionType;
    private double amount;

}
