package com.mobile.banking.Customer.service;

import com.mobile.banking.Customer.constants.ApplicationConstants;
import com.mobile.banking.Customer.constants.TransactionEnum;
import com.mobile.banking.Customer.dto.ResponseDTO;
import com.mobile.banking.Customer.entity.Customer;
import com.mobile.banking.Customer.entity.CustomerTransaction;
import com.mobile.banking.Customer.exception.AccountDoesNotExistException;
import com.mobile.banking.Customer.exception.InsufficientFundsException;
import com.mobile.banking.Customer.exception.OperationNotAllowedException;
import com.mobile.banking.Customer.repository.CustomerRepository;
import com.mobile.banking.Customer.repository.TransactionRepository;
import com.mobile.banking.Customer.utility.CustomerUtility;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Transactional
    public ResponseDTO depositAmount(String accountNumber, double amount) {
        try {
            Customer customer = getCustomerByAccountNumber(accountNumber);

            if (!CustomerUtility.getCustomerIdFromToken().equals(customer.getCustomerId())) {
                throw new OperationNotAllowedException(ApplicationConstants.UNAUTHORIZED);
            }

            customer.setAccountBalance(customer.getAccountBalance() + amount);
            customer = customerRepository.save(customer);

            getCustomerTransaction(accountNumber, amount, TransactionEnum.DEPOSIT.toString());

            return new ResponseDTO(ApplicationConstants.SUCCESS, "Deposit is successful");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public ResponseDTO withdrawAmount(String accountNumber, double amount) {
        Customer customer = null;
        try {
            customer = getCustomerByAccountNumber(accountNumber);
            if (!CustomerUtility.getCustomerIdFromToken().equals(customer.getCustomerId())) {
                throw new OperationNotAllowedException(ApplicationConstants.UNAUTHORIZED);
            }
            if (!customer.getAccountType().equals("SAVINGS")) {
                if (customer.getAccountBalance() < amount) {
                    throw new InsufficientFundsException(ApplicationConstants.INSUFFICIENT_FUNDS);
                }
                customer.setAccountBalance(customer.getAccountBalance() - amount);
                customer = customerRepository.save(customer);

                getCustomerTransaction(accountNumber, amount, TransactionEnum.WITHDRAW.toString());

                return new ResponseDTO(ApplicationConstants.SUCCESS, "Withdrawal is successful");
            } else {
                throw new OperationNotAllowedException(ApplicationConstants.NOT_ALLOWED_OPERATION);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public ResponseDTO transferFunds(String senderAccountNumber, String receiverAccountNumber, double amount) {
        Customer senderCustomer;
        Customer receiverCustomer;
        try {
            senderCustomer = getCustomerByAccountNumber(senderAccountNumber);

            receiverCustomer = getCustomerByAccountNumber(receiverAccountNumber);

            if (!CustomerUtility.getCustomerIdFromToken().equals(senderCustomer.getCustomerId())) {
                throw new OperationNotAllowedException(ApplicationConstants.UNAUTHORIZED);
            }

            if (!senderCustomer.getAccountType().equals("SAVINGS")) {

                if (senderCustomer.getCustomerId().equals(receiverCustomer.getCustomerId())) {
                    if (amount > 100000) {
                        throw new OperationNotAllowedException(ApplicationConstants.NOT_ALLOWED_OPERATION);
                    }
                } else {
                    if (amount > 15000 || senderCustomer.getAccountBalance() < amount) {
                        throw new OperationNotAllowedException(ApplicationConstants.NOT_ALLOWED_OPERATION);
                    }
                }

                if(senderCustomer.getAccountBalance() < amount) {
                    throw new OperationNotAllowedException(ApplicationConstants.INSUFFICIENT_FUNDS);
                }

                withdrawAmount(senderAccountNumber, amount);
                depositAmount(receiverAccountNumber, amount);

                getCustomerTransaction(senderAccountNumber, amount, TransactionEnum.TRANSFER.toString());

                return new ResponseDTO(ApplicationConstants.SUCCESS, "Transfer of funds is successful");

            } else {
                throw new OperationNotAllowedException(ApplicationConstants.NOT_ALLOWED_OPERATION);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public Customer getCustomerByAccountNumber(String accountNumber) throws AccountDoesNotExistException {
        Customer customer;
        customer = customerRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountDoesNotExistException(ApplicationConstants.ACCOUNT_NOT_EXISTS));
        return customer;
    }

    public CustomerTransaction getCustomerTransaction(String accountNumber, double amount, String transactionType) {
        CustomerTransaction customerTransaction = new CustomerTransaction();
        customerTransaction.setAccountNumber(accountNumber);
        customerTransaction.setAmount(amount);
        customerTransaction.setTransactionType(transactionType);
        customerTransaction = transactionRepository.save(customerTransaction);
        return customerTransaction;
    }
}
