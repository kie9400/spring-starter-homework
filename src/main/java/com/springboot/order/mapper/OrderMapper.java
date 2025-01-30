package com.springboot.order.mapper;

import com.springboot.coffee.entitiy.Coffee;
import com.springboot.order.dto.*;
import com.springboot.order.entity.Order;
import com.springboot.order.entity.OrderCoffee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    default OrderCoffeeResponseDto orderCoffeeToOrderOrderCoffeeResponseDto(OrderCoffee orderCoffee){
        OrderCoffeeResponseDto responseDto = new OrderCoffeeResponseDto(
                orderCoffee.getCoffee().getCoffeeId(),
                orderCoffee.getCoffee().getKorName(),
                orderCoffee.getCoffee().getEngName(),
                orderCoffee.getCoffee().getPrice(),
                orderCoffee.getQuantity()
        );
        return responseDto;
    }

    default Order orderPostDtoToOrder(OrderPostDto orderPostDto){
        Order order = new Order();
        order.setMember(orderPostDto.getMember());
        List<OrderCoffee> orderCoffees = orderPostDto.getOrderCoffees().stream()
                .map(orderCoffeeDto -> {
                    Coffee coffee = new Coffee();
                    coffee.setCoffeeId(orderCoffeeDto.getCoffeeId());
                    OrderCoffee orderCoffee = new OrderCoffee();
                    orderCoffee.setCoffee(coffee);
                    orderCoffee.setOrder(order);
                    orderCoffee.setQuantity(orderCoffeeDto.getQuantity());
                    return orderCoffee;
                })
                .collect(Collectors.toList());
        order.setOrderCoffees(orderCoffees);
        return order;
    }
    Order orderPatchDtoToOrder(OrderPatchDto orderPatchDto);
    OrderResponseDto orderToOrderResponseDto(Order order);
    List<OrderResponseDto> ordersToOrderResponseDtos(List<Order> orders);
    OrderCoffee orderCoffeeDtoToOrderCoffee(OrderCoffeeDto orderCoffeeDto);

}
