package com.example.demo.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // Lấy thông tin xác thực từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = "ANONYMOUS"; // Mặc định là người dùng ẩn danh
        String roles = "[]";

        // Nếu người dùng đã được xác thực (đăng nhập)
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            username = authentication.getName();
            roles = authentication.getAuthorities().toString();
        }

        // Ghi log cho MỌI request với đầy đủ thông tin
        log.info("INCOMING REQUEST: Method={}, URI={}, From IP={}, User={}, Roles={}",
                request.getMethod(),
                request.getRequestURI(),
                request.getRemoteAddr(),
                username,
                roles);

        // Cho phép request đi tiếp đến các filter hoặc controller tiếp theo
        filterChain.doFilter(request, response);
    }
}