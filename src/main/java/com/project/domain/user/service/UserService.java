package com.project.domain.user.service;

import com.project.domain.user.dto.UserLoginRequestDto;
import com.project.domain.user.dto.UserPatchRequestDto;
import com.project.domain.user.dto.UserPostRequestDto;
import com.project.domain.user.entity.User;
import com.project.domain.user.mapper.UserMapper;
import com.project.domain.user.repository.UserRepository;
import com.project.global.error.exception.BusinessException;
import com.project.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public User loginUser(UserLoginRequestDto userLoginRequestDto) {
        User user = User.builder()
                .email(userLoginRequestDto.getEmail())
                .password(userLoginRequestDto.getPassword())
                .build();
        userRepository.save(user);
        return user;
    }
    public User createUser(UserPostRequestDto userPostRequestDto) {
        User user = User.builder()
                .email(userPostRequestDto.getEmail())
                .password(userPostRequestDto.getPassword())
                .build();
        userRepository.save(user);
        return user;
    }

    public User getUser(Long id) {
        return verifiedUser(id);
    }

    public List<User> getUserList() {
        return userRepository.findAll();
    }

    public User patchUser(Long id, UserPatchRequestDto userPatchRequestDto) {
        User user = verifiedUser(id).patchUser(userPatchRequestDto);
        userRepository.save(user);
        return user;
    }

    public void deleteUser(Long id) {
        userRepository.delete(verifiedUser(id));
    }

    public User verifiedUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));
    }

}
