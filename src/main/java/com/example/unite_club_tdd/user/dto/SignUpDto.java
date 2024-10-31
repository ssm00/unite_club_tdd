package com.example.unite_club_tdd.user.dto;

import com.example.unite_club_tdd.user.repository.entity.Role;
import com.example.unite_club_tdd.user.repository.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class SignUpDto {
    private String logInId;
    private String name;
    private String password;
    private Role role;


    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Res {
        private Long userId;
        private String name;
        private Role role;

        public static Res mapEntityToDTO(UserEntity entity) {
                return Res.builder()
                    .userId(entity.getUserId())
                    .name(entity.getName())
                    .role(entity.getRole())
                    .build();
        }
    }
}
