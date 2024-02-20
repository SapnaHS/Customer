package com.mobile.banking.Customer.service;

import com.mobile.banking.Customer.constants.ApplicationConstants;
import com.mobile.banking.Customer.dto.ResponseDTO;
import com.mobile.banking.Customer.entity.Customer;
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

        assertEquals(ApplicationConstants.SUCCESS, responseDTO.getStatus());
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

        assertEquals(ApplicationConstants.SUCCESS, responseDTO.getStatus());
        assertEquals("Withdrawal is successful", responseDTO.getMessage());
        assertEquals(5330.0, customer.getAccountBalance());
    }

    @Test
    void testTransferFunds() {
        String senderAccountNumber = "123456";
        String receiverAccountNumber = "34827";
        double amount = 44670.00;
        String customerId = "238897";
        Customer senderCustomer = new Customer();
        senderCustomer.setCustomerId(customerId);
        senderCustomer.setAccountType("REGULAR");
        senderCustomer.setAccountNumber(senderAccountNumber);
        senderCustomer.setAccountBalance(50000.0);

        Customer receiverCustomer = new Customer();
        receiverCustomer.setCustomerId(customerId);
        receiverCustomer.setAccountType("REGULAR");
        receiverCustomer.setAccountNumber(senderAccountNumber);
        receiverCustomer.setAccountBalance(10000.0);

        when(customerRepository.findByAccountNumber(senderAccountNumber)).thenReturn(Optional.of(senderCustomer));
        when(customerRepository.findByAccountNumber(receiverAccountNumber)).thenReturn(Optional.of(receiverCustomer));
        when(CustomerUtility.getCustomerIdFromToken()).thenReturn(customerId);
        when(customerRepository.save(senderCustomer)).thenReturn(senderCustomer);
        when(customerRepository.save(receiverCustomer)).thenReturn(receiverCustomer);

        ResponseDTO responseDTO = transactionService.transferFunds(senderAccountNumber, receiverAccountNumber, amount);

        assertEquals(ApplicationConstants.SUCCESS, responseDTO.getStatus());
        assertEquals("Transfer of funds is successful", responseDTO.getMessage());
        assertEquals(5330.0, senderCustomer.getAccountBalance());
    }
}
