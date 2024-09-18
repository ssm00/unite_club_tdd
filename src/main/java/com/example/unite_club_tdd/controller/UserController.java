package com.example.unite_club_tdd.controller;


import com.example.unite_club_tdd.dto.BaseResponseStatus;
import com.example.unite_club_tdd.dto.LoginDto;
import com.example.unite_club_tdd.dto.ResponseDTO;
import com.example.unite_club_tdd.dto.SignUpDto;
import com.example.unite_club_tdd.exception.BaseException;
import com.example.unite_club_tdd.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/")
public class UserController {

    private final UserService userService;

    @PostMapping("signUp")
    public ResponseEntity<ResponseDTO> registerUser(@RequestBody SignUpDto signUpDto) {
        try {
            SignUpDto.Res res = userService.signUp(signUpDto);
            return ResponseEntity.status(BaseResponseStatus.SUCCESS.getStatus())
                    .body(new ResponseDTO<>(BaseResponseStatus.SUCCESS, res));
        } catch (BaseException e) {
            return ResponseEntity.status(e.getBaseResponseStatus().getStatus())
                    .body(new ResponseDTO<>(e.getBaseResponseStatus()));
        }
    }

    @PostMapping("login")
    public ResponseEntity<ResponseDTO> loginUser(@RequestBody LoginDto loginDto) {
        try {
            LoginDto.Res res = userService.login(loginDto);
            return ResponseEntity.status(BaseResponseStatus.SUCCESS.getStatus())
                    .body(new ResponseDTO<>(BaseResponseStatus.SUCCESS, res));
        } catch (BaseException e) {
            return ResponseEntity.status(e.getBaseResponseStatus().getStatus())
                    .body(new ResponseDTO<>(e.getBaseResponseStatus()));
        }
    }
}
