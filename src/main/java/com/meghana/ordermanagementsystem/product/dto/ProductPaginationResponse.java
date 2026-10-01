package com.meghana.ordermanagementsystem.product.dto;

import com.meghana.ordermanagementsystem.common.dto.PaginationResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public class ProductPaginationResponse extends PaginationResponse<ProductResponse> {
    public ProductPaginationResponse(Page<?> page, List<ProductResponse> content) {
        super(page, content);
    }
}
