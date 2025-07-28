package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
// Đổi tên bảng thành "_user" để tránh trùng với từ khóa "user" trong một số hệ CSDL
@Table(name = "_user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstname;
    private String lastname;
    @Column(unique = true) // Email phải là duy nhất
    private String email;
    private String password;

    @Enumerated(EnumType.STRING) // Lưu role dưới dạng String (USER, ADMIN)
    private Role role;

    // --- Triển khai các phương thức của UserDetails ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Trả về danh sách quyền của người dùng
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        // Chúng ta sẽ dùng email làm username để đăng nhập
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Tài khoản không bao giờ hết hạn
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Tài khoản không bị khóa
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Mật khẩu không bao giờ hết hạn
    }

    @Override
    public boolean isEnabled() {
        return true; // Tài khoản được kích hoạt
    }
}