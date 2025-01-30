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
        //회원이 존재하는지 확인
        Member findMember = memberService.findVerifiedMember(order.getMember().getMemberId());

        //커피가 존재하는지 확인
        order.getOrderCoffees().stream()
                .forEach(orderCoffee -> coffeeService.findVerifiedCoffee(orderCoffee.getCoffee().getCoffeeId()));


        //스탬프 증가
        findMember.getStamp().setStampCount(addStampCount(order));
        return orderRepository.save(order);
    }

    public Order updateOrder(Order order){
        Order findOrder = findVerifiedOrder(order.getOrderId());

        //주문 상태만 수정이 가능하다.
        //주문 상태가 요청으로 들어온다면(null이 아니라면) 수정한다.
        Optional.ofNullable(order.getOrderStatus())
                .ifPresent(orderStatus -> findOrder.setOrderStatus(orderStatus));

        //repository에 저장하여 반영
        return orderRepository.save(order);
    }


    public Order findOrder(long orderId) {
        return findVerifiedOrder(orderId);
    }

    public Page<Order> findOrders(int page, int size) {
        return orderRepository.findAll(PageRequest.of(page, size,
                Sort.by("orderId").descending()));
    }

    public void cancelOrder(long orderId) {
        //데이터베이스 에서 삭제하지않고 상태만 변경
        Order order = findVerifiedOrder(orderId);
        order.setOrderStatus(Order.OrderStatus.ORDER_CANCEL);
        orderRepository.save(order);

        //orderRepository.delete(order);
    }

    //회원이 주문했을경우 stampCount를 증가시킨다.
    public int addStampCount(Order order){
        int stampCount = order.getOrderCoffees().stream()
                .mapToInt(orderCoffee -> orderCoffee.getQuantity())
                .sum();

        return stampCount;
    }

    //해당 주문이 존재하는지 확인
    private Order findVerifiedOrder(long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);

        Order findOrder = order.orElseThrow(()->
                new BusinessLogicException(ExceptionCode.ORDER_NOT_FOUND));

        return findOrder;
    }

}
