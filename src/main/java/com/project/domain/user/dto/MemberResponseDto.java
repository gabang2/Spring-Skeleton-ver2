package com.project.domain.member.dto;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {
    public String email;
    public String password;
    public LocalDateTime createdDate;
    public LocalDateTime updatedDate;
}