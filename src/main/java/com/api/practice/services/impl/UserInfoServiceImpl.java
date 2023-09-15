package com.api.practice.services.impl;

import com.api.practice.dto.UserInfoDto;
import com.api.practice.entities.UserInfo;
import com.api.practice.exceptions.UserNotFoundException;
import com.api.practice.repositories.UserInfoRepository;
import com.api.practice.services.UserInfoService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional(rollbackOn = Exception.class)
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserInfoDto addUser(UserInfoDto userInfoDto) {
        userInfoDto.setPassword(passwordEncoder.encode(userInfoDto.getPassword()));
        UserInfo userInfo = userInfoDtoToUserInfo(userInfoDto);
        UserInfo saved = repository.save(userInfo);
        return userInfoToUserInfoDto(saved);
    }

    @Override
    public UserInfoDto getUserById(Integer userId) {
        UserInfo userById = repository.findById(userId)
                .orElseThrow(()->new UserNotFoundException("User with user id "+ userId + "not found"));

        return userInfoToUserInfoDto(userById);
    }

    @Override
    public List<UserInfoDto> getAllUsers() {
        List<UserInfo> allUsers = repository.findAll();
        List<UserInfoDto> allUsersDto = new ArrayList<>();
        allUsers.forEach(userInfo -> allUsersDto.add(userInfoToUserInfoDto(userInfo)));
        return allUsersDto;
    }

    @Override
    public UserInfoDto updateUser(UserInfoDto userInfoDto, Integer userId) {
        UserInfo userById = repository.findById(userId)
                .orElseThrow(()->new UserNotFoundException("User with user id "+ userId + "not found"));
        userById.setName(userInfoDto.getName());
        userById.setEmail(userInfoDto.getEmail());
        userById.setPassword(userInfoDto.getPassword());
        userById.setRoles(userInfoDto.getRoles());
        UserInfo updatedUser = repository.save(userById);

        return userInfoToUserInfoDto(updatedUser);
    }

    @Override
    public boolean deleteUserById(Integer userId) {
        UserInfo userById = repository.findById(userId)
                .orElseThrow(()->new UserNotFoundException("User with user id "+ userId + "not found"));
        if(userById != null){
            repository.deleteById(userId);
            return true;
        }
        return false;
    }

    private UserInfo userInfoDtoToUserInfo(UserInfoDto userInfoDto){
        UserInfo userInfo = new UserInfo();
        userInfo.setName(userInfoDto.getName());
        userInfo.setEmail(userInfoDto.getEmail());
        userInfo.setPassword(userInfoDto.getPassword());
        userInfo.setRoles(userInfoDto.getRoles());
        return userInfo;
    }

    private UserInfoDto userInfoToUserInfoDto(UserInfo userInfo){
        return new UserInfoDto(userInfo.getName(), userInfo.getPassword(), userInfo.getEmail(), userInfo.getRoles());
    }
}
