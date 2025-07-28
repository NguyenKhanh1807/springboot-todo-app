package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo")
public class DemoController {

    @GetMapping("/profile")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ApiResponse<String> userProfile() {
        // Returns the standard ApiResponse
        return ApiResponse.<String>builder()
                .result("Xin chào, đây là trang profile của bạn!")
                .build();
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<String> adminOnlyData() {
        // Returns the standard ApiResponse
        return ApiResponse.<String>builder()
                .result("Đây là dữ liệu siêu bí mật chỉ dành cho Admin!")
                .build();
    }
}