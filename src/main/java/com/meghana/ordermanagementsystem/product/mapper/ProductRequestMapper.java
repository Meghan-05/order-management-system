package com.meghana.ordermanagementsystem.product.mapper;

import com.meghana.ordermanagementsystem.common.mapper.EntityDtoMapper;
import com.meghana.ordermanagementsystem.product.dto.ProductRequest;
import com.meghana.ordermanagementsystem.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductRequestMapper extends EntityDtoMapper<Product, ProductRequest> {
}
