package com.springboot.coffee.entitiy;

import com.springboot.audit.Auditable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Coffee extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long coffeeId;

    @Column(length = 20, nullable = false)
    private String korName;

    @Column(length = 20, nullable = false)
    private String engName;

    @Column(length = 3, nullable = false)
    private String coffeeCode;

    @Column(nullable = false)
    private Integer price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CoffeeStatus coffeeStatus = CoffeeStatus.COFFEE_SALE;

    public enum CoffeeStatus{
        COFFEE_SALE("판매 중"),
        COFFEE_SOLD("품절");

        @Getter
        private String status;

        CoffeeStatus(String status) {
            this.status = status;
        }
    }
}
