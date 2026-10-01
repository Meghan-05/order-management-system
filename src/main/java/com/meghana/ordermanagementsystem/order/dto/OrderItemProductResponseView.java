package com.meghana.ordermanagementsystem.order.dto;

import lombok.Data;

@Data
public class OrderItemProductResponseView {
    private Long id;

    private String name;

    private String sku;
}
