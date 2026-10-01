package com.meghana.ordermanagementsystem.order.mappers;

import com.meghana.ordermanagementsystem.common.mapper.EntityDtoMapper;
import com.meghana.ordermanagementsystem.order.dto.OrderItemResponseView;
import com.meghana.ordermanagementsystem.order.dto.OrderResponse;
import com.meghana.ordermanagementsystem.order.entity.Order;
import com.meghana.ordermanagementsystem.order.entity.OrderItem;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderResponseMapper extends EntityDtoMapper<Order, OrderResponse> {
    @Override
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "orderItems", source = "items")
    @Mapping(target = "customer", ignore = true)
    Order dtoToEntity(OrderResponse dto);

    @Override
    @Mapping(target = "items", source = "orderItems")
    OrderResponse entityToDto(Order order);

    OrderItemResponseView toOrderItemResponse(OrderItem orderItem);
}
