package com.springboot.member.entity;

import com.springboot.audit.Auditable;
import com.springboot.order.entity.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Member extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false, unique = true, updatable = false)
    private String email;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(nullable = false, length = 13)
    private String phone;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus memberStatus = MemberStatus.MEMBER_ACTIVE;

    //mappedBy는 참조할 대상을 작성하며, 양방향 매핑에서만 사용
    //order에서 외래키 역할을 하는 member필드를 뜻함
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    //스탬프 객체와 1대1 연관관계 설정, 모든 Cascade(영속성전이)를 적용
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "STAMP_ID")
    private Stamp stamp;

    //영속성 전이 (연관관계 수동설정)
    public void setOrder(Order order){
        orders.add(order);

        if(order.getMember() != this){
            order.setMember(this);
        }
    }

    public enum MemberStatus{
        MEMBER_ACTIVE("활동 상태"),
        MEMBER_SLEEP("휴면 상태"),
        MEMBER_QUIT("탈퇴 상태");

        @Getter
        private String status;

        MemberStatus(String status){
            this.status = status;
        }
    }
}
