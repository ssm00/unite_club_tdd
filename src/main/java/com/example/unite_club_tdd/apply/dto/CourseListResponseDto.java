package com.example.unite_club_tdd.apply.dto;

import com.example.unite_club_tdd.apply.repository.entity.Course;

import java.util.List;

public record CourseListResponseDto(List<Course> courses) {

}
