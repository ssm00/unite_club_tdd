package com.example.unite_club_tdd.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ResponseDTO<T> {

    private Integer statusVal;
    private String message;
    private HttpStatus status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    public ResponseDTO(BaseResponseStatus status, T result) {
        this.statusVal = status.getStatus().value();
        this.message = status.getMessage();
        this.status = status.getStatus();
        this.result = result;
    }

    public ResponseDTO(BaseResponseStatus status) {
        this.statusVal = status.getStatus().value();
        this.status = status.getStatus();
        this.message = status.getMessage();
    }

}
