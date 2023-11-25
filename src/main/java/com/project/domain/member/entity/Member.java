package com.project.domain.member.entity;

import com.project.domain.member.dto.MemberPatchRequestDto;
import com.project.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Entity(name = "member")
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    public Long id;

    @Column(name = "email")
    public String email;

    @Column(name = "password")
    public String password;

    @Builder
    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Patch
    public Member patchMember(MemberPatchRequestDto memberPatchRequestDto) {
        this.email = Optional.ofNullable(memberPatchRequestDto.getEmail()).orElse(this.email);
        this.password = Optional.ofNullable(memberPatchRequestDto.getPassword()).orElse(this.password);
        return this;
    }
}
