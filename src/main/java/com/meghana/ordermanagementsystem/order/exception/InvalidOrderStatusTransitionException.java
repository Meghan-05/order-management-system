package com.meghana.ordermanagementsystem.order.exception;

import com.meghana.ordermanagementsystem.common.exception.BusinessException;
import com.meghana.ordermanagementsystem.order.enums.OrderStatus;
import org.springframework.http.HttpStatus;

public class InvalidOrderStatusTransitionException extends BusinessException {
    public InvalidOrderStatusTransitionException(OrderStatus from, OrderStatus to) {
        super("Order status cannot be changed from status %s to %s".formatted(from, to), HttpStatus.CONFLICT);
    }

    public InvalidOrderStatusTransitionException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
