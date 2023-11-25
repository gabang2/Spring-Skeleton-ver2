package com.project.domain.user.entity;

import com.project.domain.user.dto.UserPatchRequestDto;
import com.project.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Entity(name = "user")
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    public Long id;

    @Column(name = "email")
    public String email;

    @Column(name = "password")
    public String password;

    @Column(name = "refresh_token")
    public String refreshToken;

    @Builder
    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.refreshToken = null;
    }

    // Patch
    public User patchUser(UserPatchRequestDto userPatchRequestDto) {
        this.email = Optional.ofNullable(userPatchRequestDto.getEmail()).orElse(this.email);
        this.password = Optional.ofNullable(userPatchRequestDto.getPassword()).orElse(this.password);
        this.refreshToken = Optional.ofNullable(userPatchRequestDto.getRefreshToken()).orElse(this.refreshToken);
        return this;
    }
}
