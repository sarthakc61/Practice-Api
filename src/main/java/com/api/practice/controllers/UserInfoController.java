package com.api.practice.controllers;

import com.api.practice.dto.UserInfoDto;
import com.api.practice.services.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@Validated
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;


    @Operation(
            description = "Post endpoint for adding users which can generate jwt tokens",
            summary = "Adding users for authentication purpose",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Internal Server error",
                            responseCode = "500"
                    )
            }

    )
    @PostMapping(value = "/addUser", consumes = "application/json" , produces = "application/json")
    public ResponseEntity<UserInfoDto> addUser(@Valid @RequestBody UserInfoDto userInfoDto){
        UserInfoDto userDto = userInfoService.addUser(userInfoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    @GetMapping(value = "/getAllUsers", produces = "application/json")
    public ResponseEntity<List<UserInfoDto>> getAllUsers(){
        List<UserInfoDto> allUsersDto = userInfoService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(allUsersDto);
    }

    @GetMapping(value = "/getUserById/{userId}", produces = "application/json")
    public ResponseEntity<UserInfoDto> getUserById(@Min(value = 0, message = "Id must be greater than 0")
                                                       @PathVariable Integer userId){
        UserInfoDto userDto = userInfoService.getUserById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

    @PutMapping(value = "/updateUser/{userId}", consumes = "application/json" , produces = "application/json")
    public ResponseEntity<UserInfoDto> updateUserById(@Min(value = 0, message = "Id must be greater than 0")
                                                          @RequestBody UserInfoDto userInfoDto, Integer userId){
        UserInfoDto userDto = userInfoService.updateUser(userInfoDto, userId);
        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping(value = "/deleteUserById/{userId}", produces = "application/json")
    public ResponseEntity<String> deleteUserById(@Min(value = 0, message = "Id must be greater than 0")
                                                     @PathVariable Integer userId){
        userInfoService.deleteUserById(userId);
        return ResponseEntity.ok("User with user id : " + userId + " deleted successfully!!");
    }
}
