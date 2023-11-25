package com.project.domain.member.service;

import com.project.domain.member.dto.MemberLoginRequestDto;
import com.project.domain.member.dto.MemberPatchRequestDto;
import com.project.domain.member.dto.MemberPostRequestDto;
import com.project.domain.member.entity.Member;
import com.project.domain.member.mapper.MemberMapper;
import com.project.domain.member.repository.MemberRepository;
import com.project.global.error.exception.BusinessException;
import com.project.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    public Member loginMember(MemberLoginRequestDto memberLoginRequestDto) {
        Member member = Member.builder()
                .email(memberLoginRequestDto.getEmail())
                .password(memberLoginRequestDto.getPassword())
                .build();
        memberRepository.save(member);
        return member;
    }
    public Member createMember(MemberPostRequestDto memberPostRequestDto) {
        Member member = Member.builder()
                .email(memberPostRequestDto.getEmail())
                .password(memberPostRequestDto.getPassword())
                .build();
        memberRepository.save(member);
        return member;
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

}
