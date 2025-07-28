package com.example.demo.service;

import com.example.demo.dto.RegisterRequest;
import com.example.demo.model.Role;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Kích hoạt Mockito
class AuthenticationServiceTest {

    @Mock // 1. Tạo một đối tượng giả của UserRepository
    private UserRepository userRepository;

    @Mock // 2. Tạo một đối tượng giả của PasswordEncoder
    private PasswordEncoder passwordEncoder;

    @Mock // 3. Tạo một đối tượng giả của JwtService
    private JwtService jwtService;

    @InjectMocks // 4. Tiêm các đối tượng giả (mock) ở trên vào AuthenticationService thật
    private AuthenticationService authenticationService;

    private RegisterRequest registerRequest;

    @BeforeEach // Phương thức này sẽ chạy trước mỗi @Test
    void setUp() {
        // Chuẩn bị dữ liệu đầu vào
        registerRequest = RegisterRequest.builder()
                .firstname("Test")
                .lastname("User")
                .email("test@example.com")
                .password("password")
                .build();
    }

    @Test
    void whenRegister_shouldReturnToken() {
        // GIVEN (ARRANGE) - Thiết lập hành vi cho các mock

        // Khi phương thức `encode` được gọi với bất kỳ chuỗi nào,
        // nó sẽ trả về chuỗi "encodedPassword"
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");

        // Khi phương thức `save` được gọi với bất kỳ UserEntity nào,
        // nó sẽ không làm gì cả, chỉ trả về chính đối tượng đó
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Khi phương thức `generateToken` được gọi,
        // nó sẽ trả về chuỗi "mocked-jwt-token"
        when(jwtService.generateToken(any(UserEntity.class))).thenReturn("mocked-jwt-token");

        // WHEN (ACT) - Gọi phương thức cần test
        var response = authenticationService.register(registerRequest);

        // THEN (ASSERT) - Kiểm tra kết quả
        assertNotNull(response.getToken(), "Token không được null");
    }
}