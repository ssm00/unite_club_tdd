package com.example.unite_club_tdd.apply.controller;

import com.example.unite_club_tdd.apply.application.ApplyService;
import com.example.unite_club_tdd.apply.dto.MySucceedApplyResponseDto;
import com.example.unite_club_tdd.apply.dto.ApplyRequestDto;
import com.example.unite_club_tdd.apply.dto.ApplyResponseDto;
import com.example.unite_club_tdd.apply.dto.CourseListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ApplyController {

    private final ApplyService applyService;

    @PostMapping("/apply")
    public ApplyResponseDto apply(@RequestBody ApplyRequestDto applyRequestDto) {
        return applyService.apply(applyRequestDto);
    }

    @GetMapping("/apply/{userId}")
    public MySucceedApplyResponseDto getMyApplyList(@PathVariable Long userId) {
        return applyService.getMySucceedApply(userId);
    }

    @GetMapping("/course")
    public CourseListResponseDto getApplyList(@RequestParam(value = "available", required = false) Boolean available) {
        if (available != null) {
            return applyService.getCourseList(available);
        }
        return applyService.getAllCourseList();
    }

}
