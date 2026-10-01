package com.meghana.ordermanagementsystem.product.exception;

import com.meghana.ordermanagementsystem.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NegativeStockQuantityException extends BusinessException {
    public NegativeStockQuantityException() {
        super("Product stock quantity must be positive", HttpStatus.CONFLICT);
    }

    public NegativeStockQuantityException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
