package com.mobile.banking.Customer.controller;

import com.mobile.banking.Customer.dto.ResponseDTO;
import com.mobile.banking.Customer.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/{accountNumber}/deposit")
    public ResponseDTO depositAmount(@PathVariable String accountNumber, @RequestParam double amount) {

         return transactionService.depositAmount(accountNumber, amount);
    }

    @PostMapping("/{accountNumber}/withdraw")
    public ResponseDTO withdrawAmount(@PathVariable String accountNumber, @RequestParam double amount) {

            return transactionService.withdrawAmount(accountNumber, amount);

    }

    @PostMapping("/transfer")
    public ResponseDTO transferFunds(@RequestParam String fromAccountId, @RequestParam String toAccountId, @RequestParam double amount) {
         return transactionService.transferFunds(fromAccountId, toAccountId, amount);
    }
}
