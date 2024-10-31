package com.example.unite_club_tdd;

import com.example.unite_club_tdd.user.repository.entity.Role;
import com.example.unite_club_tdd.user.repository.entity.UserEntity;
import com.example.unite_club_tdd.user.dto.BaseResponseStatus;
import com.example.unite_club_tdd.user.dto.LoginDto;
import com.example.unite_club_tdd.user.dto.SignUpDto;
import com.example.unite_club_tdd.exception.BaseException;
import com.example.unite_club_tdd.user.repository.UserRepository;
import com.example.unite_club_tdd.user.application.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
public class SignInTest {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @Test
    void 로그인실패() {
        String loginId = "testUser";
        String password = "testPassword";
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(userRepository);  // 실제 UserService 객체

        // findByLoginIdAndPassword가 빈 Optional을 반환하도록 모킹
        Mockito.when(userRepository.findByLoginIdAndPassword(loginId, password))
                .thenReturn(Optional.empty());
        BaseException exception = assertThrows(BaseException.class, () -> {
            userService.login(new LoginDto(loginId, password));
        });

        // Assert: 발생한 예외의 상태와 메시지를 확인
        assertThat(exception.getBaseResponseStatus()).isEqualTo(BaseResponseStatus.ID_PW_NOT_EXIST);
    }

    @Test
    public void 로그인성공() throws BaseException {
        // Given: Mock UserRepository와 로그인 ID, 패스워드 설정
        String loginId = "testUser";
        String password = "testPassword";
        String userName = "Test USER NAME";
        Role role = Role.USER;

        // 가짜 UserEntity 생성
        UserEntity userEntity = new UserEntity(1L, loginId, userName,password, role);

        UserRepository userRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(userRepository);

        // findByLoginIdAndPassword가 Optional.of(mockUserEntity)를 반환하도록 설정
        Mockito.when(userRepository.findByLoginIdAndPassword(loginId, password))
                .thenReturn(Optional.of(userEntity));

        // When: login 메서드를 호출할 때
        LoginDto loginDto = new LoginDto(loginId, password);
        LoginDto.Res loginResponse = userService.login(loginDto);

        // Then: 로그인 성공으로 인해 null이 아닌 응답을 받아야 함
        assertNotNull(loginResponse);
        assertThat(loginResponse.getLoginId()).isEqualTo(loginId);
        assertThat(loginResponse.getRole()).isEqualTo(role);
    }

    @Test
    void 회원가입() throws BaseException {
        //given
        SignUpDto req = new SignUpDto("userID1", "userName1", "password1", Role.USER);

        //when
        SignUpDto.Res res = userService.signUp(req);

        //then
        Long resUserId = res.getUserId();

        Optional<UserEntity> findUser = userRepository.findById(resUserId);
        assertTrue(findUser.isPresent());
        UserEntity user = findUser.get();
        assertEquals(user.getUserId(), resUserId);
        assertEquals(user.getRole(), Role.USER);
        assertEquals(user.getPassword(), "password1");
    }

    @Test
    void 회원가입시_존재하는아이디() throws BaseException {
        //given
        SignUpDto req1 = new SignUpDto("userID1", "userName1", "password1", Role.USER);
        SignUpDto req2 = new SignUpDto("userID1", "userName1", "password1", Role.USER);

        //when
        SignUpDto.Res res = userService.signUp(req1);

        //then
        BaseException baseException = assertThrows(BaseException.class, () -> userService.signUp(req2));
        assertThat(baseException.getBaseResponseStatus()).isEqualTo(BaseResponseStatus.DUPLICATE_LOGINID);
    }

}


