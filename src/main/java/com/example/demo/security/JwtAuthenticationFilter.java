package com.example.demo.security; // Hoặc package config

import com.example.demo.security.JwtService; // Nhớ import JwtService của bạn
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor // Tự động tạo constructor cho các trường final
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Lấy token từ header
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 2. Kiểm tra token có tồn tại và đúng định dạng "Bearer " không
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Nếu không có token, cho qua và để các filter sau xử lý
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Tách lấy phần token
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt); // Trích xuất email từ token

        // 4. Kiểm tra user đã được xác thực chưa
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Lấy thông tin user từ database
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // Nếu token hợp lệ
            if (jwtService.isTokenValid(jwt, userDetails)) {
                // Tạo đối tượng xác thực
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // credentials
                        userDetails.getAuthorities()
                );
                // Gắn thêm chi tiết của request
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                // Cập nhật SecurityContextHolder -> Báo cho Spring Security biết user này đã được xác thực
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // Cho request đi tiếp
        filterChain.doFilter(request, response);
    }
}