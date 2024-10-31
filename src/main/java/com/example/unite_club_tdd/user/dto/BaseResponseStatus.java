package com.example.unite_club_tdd.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BaseResponseStatus {
    SUCCESS(HttpStatus.CREATED, "요청에 성공하였습니다."),
    DUPLICATE_LOGINID(HttpStatus.OK, "로그인아이디가 중복되었습니다."),
    ID_PW_NOT_EXIST(HttpStatus.OK, "일치하는 아이디 혹은 패스워드가 없습니다."),
    ;

    private final HttpStatus status;
    private final String message;

    }
