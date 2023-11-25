package com.project.domain.member.service;

import com.project.domain.member.dto.*;
import com.project.domain.member.entity.Member;
import com.project.domain.member.mapper.MemberMapper;
import com.project.domain.member.repository.MemberRepository;
import com.project.global.config.jwt.JwtTokenProvider;
import com.project.global.error.exception.BusinessException;
import com.project.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;


    /**
     * 회원가입
     */
    public MemberResponseDto registerMember(MemberRegisterRequestDto memberRegisterRequestDto) {

        // 이메일 중복 검사
        verifiedRegisterDto(memberRegisterRequestDto);

        // Member 엔티티 저장
        Member member = memberRepository.save(memberRegisterRequestDto.toEntity(bCryptPasswordEncoder));
        return memberMapper.memberToMemberResponseDto(member);
    }

    /**
     * 로그인
     */
    public MemberResponseDto loginMember(MemberLoginRequestDto memberLoginRequestDto) {

        // 이메일 & 비밀번호 일치 확인
        Member member = verifiedLoginDto(memberLoginRequestDto);

        // accessToken & refreshToken 발급
        TokenResponseDto tokenResponseDto = TokenResponseDto.of(
                jwtTokenProvider.generateAccessToken(member.getId()),
                jwtTokenProvider.generateRefreshToken(member.getId())
        );

        // Dto 반환
        MemberResponseDto memberResponseDto = memberMapper.memberToMemberResponseDto(member);
        memberResponseDto.setToken(tokenResponseDto);

        return memberResponseDto;
    }

    public Member getMember(Long id) {
        return verifiedMember(id);
    }

    public List<Member> getMemberList() {
        return memberRepository.findAll();
    }

    public Member patchMember(Long id, MemberPatchRequestDto memberPatchRequestDto) {
        Member member = verifiedMember(id).patchMember(memberPatchRequestDto);
        memberRepository.save(member);
        return member;
    }

    public void deleteMember(Long id) {
        memberRepository.delete(verifiedMember(id));
    }

    public Member verifiedMember(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    public void verifiedRegisterDto(MemberRegisterRequestDto memberRegisterRequestDto) {
        // 이메일 중복일 경우
        if (memberRepository.countByEmail(memberRegisterRequestDto.getEmail()) != 0L) {
            throw new BusinessException(ErrorCode.EMAIL_DUPLICATE);
        }
    }

    public Member verifiedLoginDto(MemberLoginRequestDto memberLoginRequestDto) {

        // 이메일 있는지 확인
        Member member = memberRepository.findMemberByEmail(memberLoginRequestDto.getEmail()).orElseThrow(() -> new BusinessException(ErrorCode.EMAIL_NOT_EXIST));

        // 비밀전호 일치 x
        if (!bCryptPasswordEncoder.matches(memberLoginRequestDto.getPassword(), member.getPassword())) {
            throw new BusinessException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        // 비밀번호 일치
        return member;
    }

    /**
     * header정보 확인해서 Member 반환
     */
    public Member getMemberFromAccessToken() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return verifiedMember(Long.parseLong(principal.toString()));
    }
}
