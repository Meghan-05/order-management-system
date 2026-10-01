package com.meghana.ordermanagementsystem.order.exception;

import com.meghana.ordermanagementsystem.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class CustomerNotActiveException extends BusinessException {
    public CustomerNotActiveException(long customerId) {
        super("Customer with customer id = %d is inactive.".formatted(customerId), HttpStatus.CONFLICT);
    }

    public CustomerNotActiveException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
