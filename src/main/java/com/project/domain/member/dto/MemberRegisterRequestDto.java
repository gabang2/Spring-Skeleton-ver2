package com.project.domain.member.dto;

import com.project.domain.member.entity.Member;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;


@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberRegisterRequestDto {

    @Email(message = "이메일 형식을 지켜야 합니다.")
    @NotBlank(message = "이메일을 입력해야 합니다.")
    private String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=?<>:])[A-Za-z\\d~!@#$%^&*()+|=]{4,16}$",
            message = "특수문자는 1개 이상 들어가야 합니다, 비밀번호 '최소 4자에서 최대 16자'까지 허용")
    private String password;

    public static MemberRegisterRequestDto of(String email, String password) {
        return new MemberRegisterRequestDto(email, password);
    }

    public Member toEntity(PasswordEncoder encoder) {
        return Member.builder()
                .email(email)
                .password(encoder.encode(password)).build();
    }
}