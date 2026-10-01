package com.meghana.ordermanagementsystem.order.dto;

import com.meghana.ordermanagementsystem.order.enums.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateOrderStatusRequest {
    @NotBlank
    private OrderStatus status;
}
