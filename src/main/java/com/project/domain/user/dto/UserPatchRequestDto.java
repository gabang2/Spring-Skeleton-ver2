package com.project.domain.user.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPatchRequestDto {
    public String email;
    public String password;
    public String refreshToken;
}