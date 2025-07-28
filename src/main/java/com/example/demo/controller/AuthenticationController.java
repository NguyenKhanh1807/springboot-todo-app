package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.AuthenticationRequest;
import com.example.demo.dto.AuthenticationResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ApiResponse<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        // Returns the standard ApiResponse
        return ApiResponse.<AuthenticationResponse>builder()
                .result(authenticationService.register(request))
                .build();
    }

    @PostMapping("/authenticate")
    public ApiResponse<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        // Returns the standard ApiResponse
        return ApiResponse.<AuthenticationResponse>builder()
                .result(authenticationService.authenticate(request))
                .build();
    }
}