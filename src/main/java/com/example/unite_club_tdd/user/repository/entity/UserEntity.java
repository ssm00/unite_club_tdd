package com.example.unite_club_tdd.user.repository.entity;

import com.example.unite_club_tdd.apply.repository.entity.Apply;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USERS")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    private String loginId;

    private String name;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Apply> applyList = new ArrayList<>();

    public UserEntity(Long userId, String loginId, String name, String password, Role role) {
        this.userId = userId;
        this.loginId = loginId;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    // Apply 엔티티를 추가하는 메서드
    public void addApply(Apply apply) {
        if (this.applyList == null) {
            this.applyList = new ArrayList<>();
        }
        this.applyList.add(apply);
        apply.setUser(this);  // 양방향 관계 설정
    }
}
