package com.project.domain.member.controller;

import com.project.domain.member.dto.MemberLoginRequestDto;
import com.project.domain.member.dto.MemberPatchRequestDto;
import com.project.domain.member.dto.MemberPostRequestDto;
import com.project.domain.member.dto.MemberResponseDto;
import com.project.domain.member.entity.Member;
import com.project.domain.member.mapper.MemberMapper;
import com.project.domain.member.service.MemberService;
import com.project.global.error.exception.BusinessException;
import com.project.global.error.exception.ErrorCode;
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

    @PostMapping("/login")
    public ResponseEntity loginMember(@RequestBody(required = false) MemberLoginRequestDto memberLoginRequestDto) {
        if (ObjectUtils.isEmpty(memberLoginRequestDto)){
            throw new BusinessException(ErrorCode.MISSING_REQUEST);
        }
        Member member = memberService.loginMember(memberLoginRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(memberMapper.memberToMemberResponseDto(member));
    }

    @PostMapping
    public ResponseEntity postMember(@RequestBody(required = false) MemberPostRequestDto memberPostRequestDto) {
        if (ObjectUtils.isEmpty(memberPostRequestDto)){
            throw new BusinessException(ErrorCode.MISSING_REQUEST);
        }
        Member member = memberService.createMember(memberPostRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(memberMapper.memberToMemberResponseDto(member));
    }

    @GetMapping("/{memberId}")
    public ResponseEntity getMember(@PathVariable(required = false) Long memberId) {
        if (ObjectUtils.isEmpty(memberId)){
            throw new BusinessException(ErrorCode.MISSING_REQUEST);
        }
        MemberResponseDto memberResponseDto = memberMapper.memberToMemberResponseDto(memberService.getMember(memberId));
        return ResponseEntity.status(HttpStatus.OK).body(memberResponseDto);
    }

    @GetMapping
    public ResponseEntity getMemberList() {
        List<MemberResponseDto> memberResponseDtoList = memberMapper.membersToMemberResponseDtos(memberService.getMemberList());
        return ResponseEntity.status(HttpStatus.OK).body(memberResponseDtoList);
    }

    @PatchMapping("/{memberId}")
    public ResponseEntity putMember(
            @PathVariable(required = false) Long memberId,
            @RequestBody(required = false) MemberPatchRequestDto memberPatchRequestDto) {
        if (ObjectUtils.isEmpty(memberId) || ObjectUtils.isEmpty(memberPatchRequestDto)){
            throw new BusinessException(ErrorCode.MISSING_REQUEST);
        }
        MemberResponseDto memberResponseDto = memberMapper.memberToMemberResponseDto(memberService.patchMember(memberId, memberPatchRequestDto));
        return ResponseEntity.status(HttpStatus.OK).body(memberResponseDto);
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity deleteMember(@PathVariable(required = false) Long memberId) {
        if (ObjectUtils.isEmpty(memberId)){
            throw new BusinessException(ErrorCode.MISSING_REQUEST);
        }
        memberService.deleteMember(memberId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
