package com.project.domain.member.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberPatchRequestDto {
    public String email;
    public String password;
    public String refreshToken;
}