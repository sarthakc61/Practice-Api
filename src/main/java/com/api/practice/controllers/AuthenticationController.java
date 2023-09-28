package com.api.practice.controllers;

import com.api.practice.dto.AuthRequest;
import com.api.practice.dto.AuthResponse;
import com.api.practice.dto.RefreshTokenRequest;
import com.api.practice.entities.RefreshToken;
import com.api.practice.services.RefreshTokenService;
import com.api.practice.services.impl.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/users/authentication")
@Tag(name = "Authentication")
public class AuthenticationController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Operation(
            description = "Post endpoint for authenticating users and generating jwt token and refresh token",
            summary = "Authenticating user based on the database entry",
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
    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticateTokenAndGetToken(@RequestBody AuthRequest authRequest){
        doAuthenticate(authRequest.getUsername(), authRequest.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        String token = jwtService.generateToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequest.getUsername());
        AuthResponse authResponse = new AuthResponse(token, refreshToken.getToken());

        return ResponseEntity.status(HttpStatus.OK).body(authResponse);
    }

    private void doAuthenticate(String username, String password) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        try{
            authenticationManager.authenticate(authenticationToken);
        }catch (BadCredentialsException ex){
            throw new RuntimeException("Invalid username or password");
        }
    }

    @Operation(
            description = "Post endpoint for generating jwt token with the help of active refresh token",
            summary = "Generate jwt and refresh token with the existing and active refresh token",
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
    @PostMapping("/refreshToken")
    public AuthResponse genrateTokenUsingRefreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest){
        return refreshTokenService.findByToken(refreshTokenRequest.getRefreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserInfo)
                .map(userInfo -> {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(userInfo.getName());
                    String token = jwtService.generateToken(userDetails);
                    return AuthResponse.builder()
                            .refreshToken(refreshTokenRequest.getRefreshToken())
                            .token(token)
                            .build();
                }).orElseThrow(()->new RuntimeException("Refresh Token is not present in database"));

    }

}
