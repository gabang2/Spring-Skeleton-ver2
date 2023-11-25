package com.project.domain.member.controller;

import com.project.domain.member.dto.*;
import com.project.domain.member.entity.Member;
import com.project.domain.member.mapper.MemberMapper;
import com.project.domain.member.service.MemberService;
import com.project.global.error.exception.BusinessException;
import com.project.global.error.exception.ErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MemberMapper memberMapper;

    /**
     * 회원가입<br>
     * request : {"email":"gabang2@gabang2.com", "password":"admin1234"}
     */
    @PostMapping("/register")
    public ResponseEntity registerMember(
            @Valid @RequestBody(required = false) MemberRegisterRequestDto memberRegisterRequestDto) {
        if (ObjectUtils.isEmpty(memberRegisterRequestDto)){
            throw new BusinessException(ErrorCode.MISSING_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.registerMember(memberRegisterRequestDto));
    }

    @PostMapping("/login")
    public ResponseEntity loginMember(
            @Valid @RequestBody(required = false) MemberLoginRequestDto memberLoginRequestDto) {
        if (ObjectUtils.isEmpty(memberLoginRequestDto)){
            throw new BusinessException(ErrorCode.MISSING_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.loginMember(memberLoginRequestDto));
    }

    @GetMapping("/reissue")
    public ResponseEntity reissueMember() {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.reissueMember());
    }

    @GetMapping
    public ResponseEntity getMember() {
        MemberResponseDto memberResponseDto = memberMapper.memberToMemberResponseDto(memberService.getMemberFromAccessToken());
        return ResponseEntity.status(HttpStatus.OK).body(memberResponseDto);
    }
}
