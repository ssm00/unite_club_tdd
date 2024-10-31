package com.example.unite_club_tdd.apply.application;

import com.example.unite_club_tdd.apply.dto.MySucceedApplyResponseDto;
import com.example.unite_club_tdd.apply.dto.ApplyRequestDto;
import com.example.unite_club_tdd.apply.dto.ApplyResponseDto;
import com.example.unite_club_tdd.apply.dto.CourseListResponseDto;
import com.example.unite_club_tdd.apply.repository.ApplyRepository;
import com.example.unite_club_tdd.apply.repository.CourseRepository;
import com.example.unite_club_tdd.apply.repository.entity.Apply;
import com.example.unite_club_tdd.apply.repository.entity.Course;
import com.example.unite_club_tdd.user.repository.UserRepository;
import com.example.unite_club_tdd.user.repository.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplyService {

    private final ApplyRepository applyRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final RedissonClient redissonClient;

    @Transactional
    public ApplyResponseDto apply(ApplyRequestDto applyRequestDto) {
        RLock lock = redissonClient.getLock("course:" + applyRequestDto.courseId());
        try {
            boolean isLocked = lock.tryLock(3, 10, TimeUnit.SECONDS);
            if (!isLocked) {
                throw new IllegalStateException("수강신청이 많아 처리할 수 없습니다. 잠시 후 다시 시도해주세요.");
            }

            //course 존재 확인
            Course course = courseRepository.findById(applyRequestDto.courseId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 과목입니다"));

            UserEntity user = userRepository.findById(applyRequestDto.userId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디 입니다"));

            //수강 가능 확인
            if (course.getCapacity() <= course.getEnrollment()) {
                throw new IllegalStateException("수강 가능 인원을 초과하였습니다");
            } else if (user.getApplyList().stream()
                    .anyMatch(apply -> apply.getCourse().getId().equals(course.getId()))) {
                throw new IllegalStateException("이미 수강신청한 과목입니다");
            }
            course.increaseEnrollment();
            //신청
            Apply apply = new Apply(true, LocalDateTime.now(), user, course);
            Apply save = applyRepository.save(apply);
            return new ApplyResponseDto(true);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("수강신청 처리 중 오류가 발생했습니다.", e);
        } catch (Exception e) {
            Course course = courseRepository.findById(applyRequestDto.courseId()).orElse(null);
            UserEntity user = userRepository.findById(applyRequestDto.userId()).orElse(null);
            Apply apply = new Apply(false, LocalDateTime.now(), user, course);
            throw e;
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    public CourseListResponseDto getAllCourseList() {
        List<Course> courses = courseRepository.findAll();
        return new CourseListResponseDto(courses);
    }

    public CourseListResponseDto getCourseList(boolean available) {
        List<Course> courses = getCourses(available);
        return new CourseListResponseDto(courses);
    }

    private List<Course> getCourses(boolean available) {
        if (available) {
            return courseRepository.selectAvailable();
        } else {
            return courseRepository.selectNotAvailable();
        }
    }
    
    //foreach는 반환값이 없음 map은 있음
    public MySucceedApplyResponseDto getMySucceedApply(Long userId) {
        UserEntity user = userRepository.findByIdWithApply(userId);
        List<Apply> applyList = user.getApplyList();
        List<Course> succeedCourses = applyList.stream()
                .filter(apply -> apply.isStatus())
                .map(apply -> apply.getCourse())
                .collect(Collectors.toList());
        return new MySucceedApplyResponseDto(userId, succeedCourses);
    }


}
