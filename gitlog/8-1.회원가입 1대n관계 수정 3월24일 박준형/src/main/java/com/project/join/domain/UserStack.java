package com.project.join.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// USER_STACK 테이블과 매핑되는 기술 스택 연결 엔티티
@Entity
@Table(name = "USER_STACK")
@NoArgsConstructor
@Setter
@Getter
public class UserStack {

    // USER_STACK 테이블의 기본 키
    @Id
    @Column(name = "USER_STACK_ID")
    private Long userStackId;

    // 어떤 사용자의 기술 스택인지 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    // TECH_STACK 테이블의 숫자형 STACK_ID 저장
    @Column(name = "STACK_ID", nullable = false)
    private Long stackId;
}
