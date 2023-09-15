package com.api.practice.services;

import com.api.practice.dto.UserInfoDto;

import java.util.List;

public interface UserInfoService {
    UserInfoDto addUser(UserInfoDto userInfoDto);
    UserInfoDto getUserById(Integer userId);
    List<UserInfoDto> getAllUsers();
    UserInfoDto updateUser(UserInfoDto userInfoDto, Integer userId);
    boolean deleteUserById(Integer userId);
}
