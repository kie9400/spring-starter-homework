package com.springboot.order.repository;

import com.springboot.order.entity.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void saveOrderTest(){
        //given
        Order order = new Order();

        //when
        Order savedOrder = orderRepository.save(order);

        //then
        assertNotNull(savedOrder);
    }
}