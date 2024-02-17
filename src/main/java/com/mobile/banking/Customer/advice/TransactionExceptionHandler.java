package com.mobile.banking.Customer.advice;

import com.mobile.banking.Customer.exception.AccountDoesNotExistException;
import com.mobile.banking.Customer.exception.InsufficientFundsException;
import com.mobile.banking.Customer.exception.OperationNotAllowedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TransactionExceptionHandler {

    @ExceptionHandler(AccountDoesNotExistException.class)
    public ProblemDetail handleAccountDoesNotExistException(AccountDoesNotExistException ex){
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,ex.getMessage());
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ProblemDetail handleInsufficientFundsException(InsufficientFundsException ex){
        return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN,ex.getMessage());
    }

    @ExceptionHandler(OperationNotAllowedException.class)
    public ProblemDetail handleOperationNotAllowedException(OperationNotAllowedException ex){
        return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN,ex.getMessage());
    }
}
