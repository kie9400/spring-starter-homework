package com.springboot.audit;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
//JPA 엔티티 클래스들이 해당 추상 클래스를 상속할 경우 createData,modifiedData를 컬럼으로 인식한다.
@MappedSuperclass
//해당 클래스에 Audithing 기능을 포함시킨다. [ JPA에서 시간에 대한 정보를 자동으로 넣어주는 기능 ]
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {
    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "modified_at")
    private LocalDateTime modifiedDate;
}
