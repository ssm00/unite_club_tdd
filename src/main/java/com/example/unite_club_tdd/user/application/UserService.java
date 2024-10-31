package com.example.unite_club_tdd.user.application;

import com.example.unite_club_tdd.user.repository.entity.Role;
import com.example.unite_club_tdd.user.repository.entity.UserEntity;
import com.example.unite_club_tdd.user.dto.BaseResponseStatus;
import com.example.unite_club_tdd.user.dto.LoginDto;
import com.example.unite_club_tdd.user.dto.SignUpDto;
import com.example.unite_club_tdd.exception.BaseException;
import com.example.unite_club_tdd.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public SignUpDto.Res signUp(SignUpDto signUpDto) throws BaseException {
        //loginID 존재 확인
        String logInId = signUpDto.getLogInId();
        String password = signUpDto.getPassword();
        String name = signUpDto.getName();
        Role role = signUpDto.getRole();
        Boolean isExist = userRepository.existsByLoginId(logInId);
        if (!isExist) {
            UserEntity user = UserEntity.builder()
                    .name(name)
                    .loginId(logInId)
                    .password(password)
                    .role(role)
                    .build();
            UserEntity save = userRepository.save(user);
            return SignUpDto.Res.mapEntityToDTO(save);
        }
        throw new BaseException(BaseResponseStatus.DUPLICATE_LOGINID);
    }

    public LoginDto.Res login(LoginDto loginDto) throws BaseException {
        String loginId = loginDto.getLoginId();
        String password = loginDto.getPassword();

        Optional<UserEntity> user = userRepository.findByLoginIdAndPassword(loginId, password);
        if (user.isPresent()) {
            UserEntity userEntity = user.get();
            return LoginDto.Res.mapEntityToDto(userEntity);
        }
        throw new BaseException(BaseResponseStatus.ID_PW_NOT_EXIST);
    }
}
