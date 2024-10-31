package com.example.unite_club_tdd.exception;

import com.example.unite_club_tdd.user.dto.BaseResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaseException extends Exception {
    private BaseResponseStatus baseResponseStatus;
}
