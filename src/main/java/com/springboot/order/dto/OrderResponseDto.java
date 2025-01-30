package com.springboot.order.dto;

import com.springboot.member.entity.Member;
import com.springboot.order.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class OrderResponseDto {
    private long orderId;
    private long memberId;
    private Order.OrderStatus orderStatus;
    private List<OrderCoffeeResponseDto> orderCoffees;
    private LocalDateTime createdAt;

    //mapper에서 파라미터로 받아온 멤버 객체의 id를 찾아서 등록한다.
    public void setMember(Member member) {
        this.memberId = member.getMemberId();
    }
}
