package com.project.domain.member.dto;
import com.project.domain.member.entity.Member;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class MemberResponseDto {
    public String email;
    public TokenResponseDto token;
    public LocalDateTime createdDate;
    public LocalDateTime updatedDate;
}