package com.example.demo.service;

import com.example.demo.dto.AuthenticationRequest;
import com.example.demo.dto.AuthenticationResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.model.Role;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        // Tạo một đối tượng UserEntity mới từ thông tin đăng ký
        var user = UserEntity.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // Mã hóa mật khẩu
                .role(Role.USER) // Gán quyền mặc định là USER
                .build();
        // Lưu người dùng vào cơ sở dữ liệu
        userRepository.save(user);
        // Tạo JWT token cho người dùng vừa đăng ký
        var jwtToken = jwtService.generateToken(user);
        // Trả về token
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // Xác thực người dùng bằng email và password
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        // Nếu xác thực thành công, tìm người dùng trong DB (chắc chắn sẽ có)
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        // Tạo JWT token cho người dùng
        var jwtToken = jwtService.generateToken(user);
        // Trả về token
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}