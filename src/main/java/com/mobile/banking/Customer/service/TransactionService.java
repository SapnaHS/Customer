package com.mobile.banking.Customer.service;

import com.mobile.banking.Customer.dto.ResponseDTO;
import com.mobile.banking.Customer.entity.Customer;
import com.mobile.banking.Customer.entity.CustomerTransaction;
import com.mobile.banking.Customer.exception.AccountDoesNotExistException;
import com.mobile.banking.Customer.exception.InsufficientFundsException;
import com.mobile.banking.Customer.exception.OperationNotAllowedException;
import com.mobile.banking.Customer.repository.CustomerRepository;
import com.mobile.banking.Customer.repository.TransactionRepository;
import com.mobile.banking.Customer.utility.CustomerUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    TransactionRepository transactionRepository;
    public ResponseDTO depositAmount(String accountNumber, double amount) {
        try {
            Customer customer = customerRepository.findByAccountNumber(accountNumber)
                    .orElseThrow(() -> new AccountDoesNotExistException("This account does not exists"));

            if(!CustomerUtility.getCustomerIdFromToken().equals(customer.getCustomerId())) {
                throw new OperationNotAllowedException("You are not authorized to do this operation");
            }

            customer.setAccountBalance(customer.getAccountBalance() + amount);
            customer = customerRepository.save(customer);

            CustomerTransaction customerTransaction = new CustomerTransaction();
            customerTransaction.setAccountNumber(accountNumber);
            customerTransaction.setAmount(amount);
            customerTransaction.setTransactionType("DEPOSIT");
            customerTransaction = transactionRepository.save(customerTransaction);

            if (customer != null && customerTransaction != null) {
                return new ResponseDTO("success", "Deposit is successful");
            } else {
                return new ResponseDTO("error", "Failed to deposit amount");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseDTO withdrawAmount(String accountNumber, double amount) {
        Customer customer = null;
        try {
            customer = customerRepository.findByAccountNumber(accountNumber)
                    .orElseThrow(() -> new AccountDoesNotExistException("This account does not exists"));
            if(!CustomerUtility.getCustomerIdFromToken().equals(customer.getCustomerId())) {
                throw new OperationNotAllowedException("You are not authorized to do this operation");
            }
            if(!customer.getAccountType().equals("SAVINGS")) {
                if(customer.getAccountBalance() < amount) {
                    throw new InsufficientFundsException("Insufficient funds to withdraw");
                }
                customer.setAccountBalance(customer.getAccountBalance() - amount);
                customer = customerRepository.save(customer);

                CustomerTransaction customerTransaction = new CustomerTransaction();
                customerTransaction.setAccountNumber(accountNumber);
                customerTransaction.setAmount(amount);
                customerTransaction.setTransactionType("WITHDRAW");
                customerTransaction = transactionRepository.save(customerTransaction);

                if (customer != null && customerTransaction != null) {
                    return new ResponseDTO("success", "Withdrawal is successful");
                } else {
                    return new ResponseDTO("error", "Failed to withdraw amount");
                }
            } else {
                throw new OperationNotAllowedException("This operation is not allowed from SAVINGS account");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseDTO transferFunds(String senderAccountNumber, String receiverAccountNumber, double amount) {
        Customer senderCustomer;
        Customer receiverCustomer;
        try {
            senderCustomer = customerRepository.findByAccountNumber(senderAccountNumber)
                    .orElseThrow(() -> new AccountDoesNotExistException("This account does not exists"));

            if(!CustomerUtility.getCustomerIdFromToken().equals(senderCustomer.getCustomerId())) {
                throw new OperationNotAllowedException("You are not authorized to do this operation");
            }

            receiverCustomer = customerRepository.findByAccountNumber(receiverAccountNumber)
                    .orElseThrow(() -> new AccountDoesNotExistException("This account does not exists"));

            if (!senderCustomer.getAccountType().equals("SAVINGS")) {

                if(senderCustomer.getCustomerId().equals(receiverCustomer.getCustomerId())) {
                    if (amount > 100000) {
                        throw new OperationNotAllowedException("This operation is not allowed on this account");
                    }
                } else {
                        if(amount > 15000) {
                            throw new OperationNotAllowedException("This operation is not allowed on this account");
                        }
                }

                withdrawAmount(senderAccountNumber, amount);
                depositAmount(receiverAccountNumber, amount);

                CustomerTransaction customerTransaction = new CustomerTransaction();
                customerTransaction.setAccountNumber(senderAccountNumber);
                customerTransaction.setAmount(amount);
                customerTransaction.setTransactionType("TRANSFER");
                customerTransaction = transactionRepository.save(customerTransaction);

                if (customerTransaction != null) {
                    return new ResponseDTO("success", "Transfer of funds is successful");
                } else {
                    return new ResponseDTO("error", "Transfer of funds failed");
                }

            } else {
                throw new OperationNotAllowedException("This operation is not allowed on this account");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
