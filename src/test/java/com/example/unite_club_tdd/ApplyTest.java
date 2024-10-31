package com.example.unite_club_tdd;

import com.example.unite_club_tdd.apply.application.ApplyService;
import com.example.unite_club_tdd.apply.dto.ApplyRequestDto;
import com.example.unite_club_tdd.apply.dto.ApplyResponseDto;
import com.example.unite_club_tdd.apply.dto.CourseListResponseDto;
import com.example.unite_club_tdd.apply.dto.MySucceedApplyResponseDto;
import com.example.unite_club_tdd.apply.repository.ApplyRepository;
import com.example.unite_club_tdd.apply.repository.CourseRepository;
import com.example.unite_club_tdd.apply.repository.entity.Apply;
import com.example.unite_club_tdd.apply.repository.entity.Course;
import com.example.unite_club_tdd.user.dto.BaseResponseStatus;
import com.example.unite_club_tdd.user.repository.UserRepository;
import com.example.unite_club_tdd.user.repository.entity.Role;
import com.example.unite_club_tdd.user.repository.entity.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

@SpringBootTest
@Transactional
public class ApplyTest {


    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ApplyService applyService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplyRepository applyRepository;

    @PersistenceContext
    private EntityManager entityManager;


    @Test
    public void 수강신청성공() {
        //given
        Course math = new Course(1L, "math", LocalTime.of(9, 30), LocalTime.of(11, 00), DayOfWeek.MONDAY, "kim", 2, 0);

        courseRepository.save(math);

        UserEntity user1 = new UserEntity(1L,"ID1","user1","1234", Role.USER);

        userRepository.save(user1);

        //when
        ApplyRequestDto mathApply1 = new ApplyRequestDto(1L, 1L, true);
        ApplyResponseDto response1 = applyService.apply(mathApply1);

        //then
        assertThat(response1.status()).isEqualTo(true);
    }


    @Test
    public void 수강신청인원초과() {
        //given
        Course math = new Course(1L, "math", LocalTime.of(9, 30), LocalTime.of(11, 00), DayOfWeek.MONDAY, "kim", 2, 2);

        courseRepository.save(math);

        UserEntity user1 = new UserEntity(1L,"ID1","user1","1234", Role.USER);

        userRepository.save(user1);

        //when
        ApplyRequestDto mathApply1 = new ApplyRequestDto(1L, 1L, true);

        //then
        IllegalStateException illegalStateException = assertThrows(IllegalStateException.class, () -> applyService.apply(mathApply1));
        assertThat(illegalStateException.getMessage()).isEqualTo("수강 가능 인원을 초과하였습니다");
    }

    @Test
    public void 전체수강신청목록조회() {
        //given
        Course math = new Course(1L, "math", LocalTime.of(9, 30), LocalTime.of(11, 00), DayOfWeek.MONDAY, "kim", 2, 0);
        Course english = new Course(2L, "english", LocalTime.of(12, 30), LocalTime.of(13, 00), DayOfWeek.MONDAY, "kim", 2, 0);
        courseRepository.save(math);
        courseRepository.save(english);

        //when
        CourseListResponseDto courseList = applyService.getAllCourseList();

        //then
        assertThat(courseList.courses().size()).isEqualTo(2);
    }

    @Test
    public void 내성공수강신청목록조회() {
        //given
        Course math = new Course(1L, "math", LocalTime.of(9, 30), LocalTime.of(11, 00), DayOfWeek.MONDAY, "kim", 2, 0);
        Course english = new Course(2L, "english", LocalTime.of(12, 30), LocalTime.of(13, 00), DayOfWeek.MONDAY, "kim", 2, 0);
        courseRepository.save(math);
        courseRepository.save(english);

        UserEntity user1 = new UserEntity(1L,"ID1","user1","1234", Role.USER);
        userRepository.save(user1);

        ApplyRequestDto applyRequestDto1 = new ApplyRequestDto(1L, 1L, true);
        ApplyRequestDto applyRequestDto2 = new ApplyRequestDto(1L, 2L, true);
        applyService.apply(applyRequestDto1);
        applyService.apply(applyRequestDto2);

        entityManager.flush();
        entityManager.clear();

        //when
        MySucceedApplyResponseDto mySucceedApply = applyService.getMySucceedApply(1L);

        //then
        assertThat(mySucceedApply.courses().size()).isEqualTo(2);
    }

    @Test
    public void 동일한수강신청() {
        //given
        Course math = new Course(1L, "math", LocalTime.of(9, 30), LocalTime.of(11, 00), DayOfWeek.MONDAY, "kim", 2, 0);

        courseRepository.save(math);

        UserEntity user1 = new UserEntity(1L,"ID1","user1","1234", Role.USER);

        userRepository.save(user1);

        //when
        ApplyRequestDto mathApply1 = new ApplyRequestDto(1L, 1L, true);
        ApplyResponseDto response1 = applyService.apply(mathApply1);

        entityManager.flush();
        entityManager.clear();

        //then
        IllegalStateException illegalStateException = assertThrows(IllegalStateException.class, () -> applyService.apply(mathApply1));
        assertThat(illegalStateException.getMessage()).isEqualTo("이미 수강신청한 과목입니다");
    }

}
