package com.project.domain.member.dto;

import com.project.domain.member.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;


@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberLoginRequestDto {

    @Email(message = "이메일 형식을 지켜야 합니다.")
    @NotBlank(message = "이메일을 입력해야 합니다.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해야 합니다.")
    private String password;

    public static MemberLoginRequestDto of(String email, String password) {
        return new MemberLoginRequestDto(email, password);
    }

    public Member toEntity(PasswordEncoder encoder) {
        return Member.builder()
                .email(email)
                .password(encoder.encode(password)).build();
    }
}