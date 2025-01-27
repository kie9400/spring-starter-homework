package com.springboot.order.service;

import com.springboot.coffee.service.CoffeeService;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.member.entity.Member;
import com.springboot.member.service.MemberService;
import com.springboot.order.entity.Order;
import com.springboot.order.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CoffeeService coffeeService;
    private final MemberService memberService;

    public OrderService(OrderRepository orderRepository, CoffeeService coffeeService, MemberService memberService) {
        this.orderRepository = orderRepository;
        this.coffeeService = coffeeService;
        this.memberService = memberService;
    }

    public Order createOrder(Order order) {
        verifyExistOrder(order);

        return order;
    }

    public Order findOrder(long orderId) {
        return findVerifiedOrder(orderId);
    }

    public Page<Order> findOrders(int page, int size) {
        return orderRepository.findAll(PageRequest.of(page, size,
                Sort.by("orderId").descending()));
    }

    public void cancelOrder(long orderId) {
        Order order = findVerifiedOrder(orderId);
        orderRepository.delete(order);
    }
    //멤버와 회원이 존재하는지 확인
    private void verifyExistOrder(Order order){
        memberService.findVerifiedMember(order.getMember().getMemberId());

        order.getOrderCoffees().stream()
                .forEach(orderCoffee ->
                        coffeeService.findCoffee(orderCoffee.getCoffee().getCoffeeId()));
    }

    //해당 주문이 존재하는지 확인
    private Order findVerifiedOrder(long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);

        Order findOrder = order.orElseThrow(()->
                new BusinessLogicException(ExceptionCode.ORDER_NOT_FOUND));

        return findOrder;
    }
}
