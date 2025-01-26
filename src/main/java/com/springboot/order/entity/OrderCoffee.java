package com.springboot.order.entity;


import com.springboot.audit.Auditable;
import com.springboot.coffee.entitiy.Coffee;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class OrderCoffee extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderCoffeeId;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "COFFEE_ID")
    private Coffee coffee;

    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    public void setOrder(Order order){
        this.order = order;

        if(order.getOrderCoffees().contains(this)){
            order.setOrderCoffee(this);
        }
    }
}
