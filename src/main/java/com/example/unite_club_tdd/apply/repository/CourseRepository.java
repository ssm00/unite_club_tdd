package com.example.unite_club_tdd.apply.repository;

import com.example.unite_club_tdd.apply.repository.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("select c from Course c where c.capacity > c.enrollment")
    public List<Course> selectAvailable();


    @Query("select c from Course c where c.capacity <= c.enrollment")
    public List<Course> selectNotAvailable();

}
