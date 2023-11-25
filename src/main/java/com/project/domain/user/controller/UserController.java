package com.project.domain.user.controller;

import com.project.domain.user.dto.UserLoginRequestDto;
import com.project.domain.user.dto.UserPatchRequestDto;
import com.project.domain.user.dto.UserPostRequestDto;
import com.project.domain.user.dto.UserResponseDto;
import com.project.domain.user.entity.User;
import com.project.domain.user.mapper.UserMapper;
import com.project.domain.user.service.UserService;
import com.project.global.error.exception.BusinessException;
import com.project.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity loginUser(@RequestBody(required = false) UserLoginRequestDto userLoginRequestDto) {
        if (ObjectUtils.isEmpty(userLoginRequestDto)){
            throw new BusinessException(ErrorCode.MISSING_REQUEST);
        }
        User user = userService.loginUser(userLoginRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.userToUserResponseDto(user));
    }

    @PostMapping
    public ResponseEntity postUser(@RequestBody(required = false) UserPostRequestDto userPostRequestDto) {
        if (ObjectUtils.isEmpty(userPostRequestDto)){
            throw new BusinessException(ErrorCode.MISSING_REQUEST);
        }
        User user = userService.createUser(userPostRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.userToUserResponseDto(user));
    }

    @GetMapping("/{userId}")
    public ResponseEntity getUser(@PathVariable(required = false) Long userId) {
        if (ObjectUtils.isEmpty(userId)){
            throw new BusinessException(ErrorCode.MISSING_REQUEST);
        }
        UserResponseDto userResponseDto = userMapper.userToUserResponseDto(userService.getUser(userId));
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }

    @GetMapping
    public ResponseEntity getUserList() {
        List<UserResponseDto> userResponseDtoList = userMapper.usersToUserResponseDtos(userService.getUserList());
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDtoList);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity putUser(
            @PathVariable(required = false) Long userId,
            @RequestBody(required = false) UserPatchRequestDto userPatchRequestDto) {
        if (ObjectUtils.isEmpty(userId) || ObjectUtils.isEmpty(userPatchRequestDto)){
            throw new BusinessException(ErrorCode.MISSING_REQUEST);
        }
        UserResponseDto userResponseDto = userMapper.userToUserResponseDto(userService.patchUser(userId, userPatchRequestDto));
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity deleteUser(@PathVariable(required = false) Long userId) {
        if (ObjectUtils.isEmpty(userId)){
            throw new BusinessException(ErrorCode.MISSING_REQUEST);
        }
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
