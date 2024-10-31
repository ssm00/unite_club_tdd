package com.example.unite_club_tdd.apply.repository.entity;

import com.example.unite_club_tdd.user.repository.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Apply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean status;
    private LocalDateTime applyAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COURSE_ID")
    private Course course;

    public Apply(boolean status, LocalDateTime applyAt, UserEntity user, Course course) {
        this.status = status;
        this.applyAt = applyAt;
        this.user = user;
        this.course = course;
    }

    public void setUser(UserEntity user) {
        this.user = user;
        if (!user.getApplyList().contains(this)) {
            user.getApplyList().add(this);
        }
    }

}
