package com.api.practice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {


    @Operation(
            description = "Get endpoint for testing role based authorization for ROLE_USER",
            summary = "Role based authorization testing",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Working fine"
                    )
            }
    )
    @GetMapping("/test")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String testAuth(){
        return "Working Fine!!";
    }
}