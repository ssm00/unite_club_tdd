package com.example.unite_club_tdd.user.dto;

import com.example.unite_club_tdd.user.repository.entity.Role;
import com.example.unite_club_tdd.user.repository.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginDto {

    private String loginId;
    private String password;

    @Builder
    @Getter
    @AllArgsConstructor
    public static class Res {
        private String loginId;
        private Role role;

        public static Res mapEntityToDto(UserEntity entity) {
            return Res.builder()
                    .loginId(entity.getLoginId())
                    .role(entity.getRole())
                    .build();
        }

    }

}


