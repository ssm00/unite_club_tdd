package com.example.unite_club_tdd.apply.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String className;
    private LocalTime startTime;
    private LocalTime endTime;
    private DayOfWeek dayOfWeek;
    private String profName;
    private Integer capacity;
    private Integer enrollment;

    @OneToMany(mappedBy = "course")
    @Builder.Default
    private List<Apply> applyList = new ArrayList<>();


    public Course(Long id, String className, LocalTime startTime, LocalTime endTime, DayOfWeek dayOfWeek, String profName, Integer capacity, Integer enrollment) {
        this.id = id;
        this.className = className;
        this.startTime = startTime;
        this.endTime = endTime;
        this.dayOfWeek = dayOfWeek;
        this.profName = profName;
        this.capacity = capacity;
        this.enrollment = enrollment;
    }

    public void increaseEnrollment() {
        this.enrollment += 1;
    }
}
