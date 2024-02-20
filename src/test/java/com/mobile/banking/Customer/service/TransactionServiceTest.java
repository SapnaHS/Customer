package com.mobile.banking.Customer.service;

import com.mobile.banking.Customer.dto.ResponseDTO;
import com.mobile.banking.Customer.entity.Customer;
import com.mobile.banking.Customer.exception.InsufficientFundsException;
import com.mobile.banking.Customer.repository.CustomerRepository;
import com.mobile.banking.Customer.repository.TransactionRepository;
import com.mobile.banking.Customer.utility.CustomerUtility;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        mockRequestContext();
    }

    private void mockRequestContext() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        RequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);
    }

    @Test
    void testDepositAmount() {
        String accountNumber = "123456";
        double amount = 5330.00;
        String customerId = "238897";
        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        customer.setAccountNumber(accountNumber);
        customer.setAccountBalance(500.0);

        when(customerRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(customer));
        when(CustomerUtility.getCustomerIdFromToken()).thenReturn(customerId);
        when(customerRepository.save(customer)).thenReturn(customer);

        ResponseDTO responseDTO = transactionService.depositAmount(accountNumber, amount);

        assertEquals("success", responseDTO.getStatus());
        assertEquals("Deposit is successful", responseDTO.getMessage());
        assertEquals(5830.0, customer.getAccountBalance());
    }

    @Test
    void testWithdrawAmount_InsufficientBalance() {
        String accountNumber = "123456";
        double amount = 5330.00;
        String customerId = "238897";
        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        customer.setAccountType("REGULAR");
        customer.setAccountNumber(accountNumber);
        customer.setAccountBalance(500.0);

        when(customerRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(customer));
        when(CustomerUtility.getCustomerIdFromToken()).thenReturn(customerId);

        assertThrows(RuntimeException.class, () -> transactionService.withdrawAmount(accountNumber, amount));
    }

    @Test
    void testWithdrawAmount_SufficientBalance() {
        String accountNumber = "123456";
        double amount = 44670.00;
        String customerId = "238897";
        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        customer.setAccountType("REGULAR");
        customer.setAccountNumber(accountNumber);
        customer.setAccountBalance(50000.0);

        when(customerRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(customer));
        when(CustomerUtility.getCustomerIdFromToken()).thenReturn(customerId);
        when(customerRepository.save(customer)).thenReturn(customer);

        ResponseDTO responseDTO = transactionService.withdrawAmount(accountNumber, amount);

        assertEquals("success", responseDTO.getStatus());
        assertEquals("Withdrawal is successful", responseDTO.getMessage());
        assertEquals(5330.0, customer.getAccountBalance());
    }

    @Test
    void testWithdrawAmount_TransferFunds() {
        String accountNumber = "123456";
        double amount = 44670.00;
        String customerId = "238897";
        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        customer.setAccountType("REGULAR");
        customer.setAccountNumber(accountNumber);
        customer.setAccountBalance(50000.0);

        when(customerRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(customer));
        when(CustomerUtility.getCustomerIdFromToken()).thenReturn(customerId);
        when(customerRepository.save(customer)).thenReturn(customer);

        ResponseDTO responseDTO = transactionService.withdrawAmount(accountNumber, amount);

        assertEquals("success", responseDTO.getStatus());
        assertEquals("Withdrawal is successful", responseDTO.getMessage());
        assertEquals(5330.0, customer.getAccountBalance());
    }
}
