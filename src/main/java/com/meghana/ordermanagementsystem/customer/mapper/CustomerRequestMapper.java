package com.meghana.ordermanagementsystem.customer.mapper;

import com.meghana.ordermanagementsystem.common.mapper.EntityDtoMapper;
import com.meghana.ordermanagementsystem.customer.dto.CustomerRequest;
import com.meghana.ordermanagementsystem.customer.entity.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerRequestMapper extends EntityDtoMapper<Customer, CustomerRequest> { }