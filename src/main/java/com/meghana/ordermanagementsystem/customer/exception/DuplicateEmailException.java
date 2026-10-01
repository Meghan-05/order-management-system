package com.meghana.ordermanagementsystem.customer.exception;

import com.meghana.ordermanagementsystem.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class DuplicateEmailException extends BusinessException {
    public DuplicateEmailException() {
        super("Customer email already exists.", HttpStatus.CONFLICT);
    }

    public DuplicateEmailException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
