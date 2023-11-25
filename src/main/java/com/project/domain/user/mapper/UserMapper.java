package com.project.domain.user.mapper;

import com.project.domain.user.dto.UserPatchRequestDto;
import com.project.domain.user.dto.UserPostRequestDto;
import com.project.domain.user.dto.UserResponseDto;
import com.project.domain.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    User userPostRequestDtoToUser(UserPostRequestDto userPostRequestDto);

    User userPatchRequestDtoToUser(UserPatchRequestDto userPatchRequestDto);

    UserResponseDto userToUserResponseDto(User user);

    List<UserResponseDto> usersToUserResponseDtos(List<User> user);
}
