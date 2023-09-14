package org.zerock.guestbook.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass   // @MappedSuerClass 는 해당 어노테이션이 적용된 클래스는 테이블로 생성하지 않는다.
@EntityListeners(value = { AuditingEntityListener.class })
// AuditingEntityListener 는 JPA 내부에서 엔티티 객체가 생성,변경되는 것을 감지하는 역할이다
// 이를 통해서 regDate, modDate에 적절한 값이 지정된다. -> main 클래스에 @EnableJpaAuditing 필수
@Getter
abstract class BaseEntity {

    @CreatedDate    // JPA에서 엔티티의 생성 시간을 처리하는 용도
    @Column(name = "regdate", updatable = false)
    // 속성으로 insertable, updatable 등이 있는데 'updatable = false'로 지정하면
    // 해당 엔티티 객체를 데이터베이스에 반영할 때 regdate 칼럼값은 변경되지 않는다.
    private LocalDateTime regDate;

    @LastModifiedDate   // JPA에서 최종 수정 시간을 자동으로 처리하는 용도
    @Column(name = "moddate" )
    private LocalDateTime modDate;


}
