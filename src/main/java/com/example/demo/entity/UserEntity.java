package com.example.demo.entity;

import com.example.demo.model.Role;
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

@Data // Tự động tạo getters, setters, toString, etc.
@Builder // Hỗ trợ tạo đối tượng theo mẫu Builder Pattern
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users") // Giữ nguyên tên bảng "users" của bạn
public class UserEntity implements UserDetails { // Thêm "implements UserDetails"

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // THAY THẾ 'name' BẰNG CÁC TRƯỜNG CỤ THỂ HƠN
    private String firstname;
    private String lastname;

    // THÊM CÁC TRƯỜNG CẦN THIẾT CHO VIỆC XÁC THỰC
    @Column(unique = true, nullable = false) // email là duy nhất và không được trống
    private String email;

    @Column(nullable = false) // password không được trống
    private String password;

    @Enumerated(EnumType.STRING) // Lưu role dưới dạng chữ (USER, ADMIN)
    private Role role;

    // GIỮ NGUYÊN: Một người dùng có nhiều Todo
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TodoEntity> todos;

    // =================================================================
    // CÁC PHƯƠN THỨC BẮT BUỘC TỪ INTERFACE "UserDetails"
    // =================================================================

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Trả về một danh sách chứa quyền (role) của người dùng
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        // Định danh của người dùng để đăng nhập -> chính là email
        return email;
    }

    @Override
    public String getPassword() {
        // Trả về mật khẩu đã được mã hóa của người dùng
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        // Tài khoản có hết hạn không? -> không
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // Tài khoản có bị khóa không? -> không
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // Mật khẩu (credentials) có hết hạn không? -> không
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Tài khoản có được kích hoạt không? -> có
        return true;
    }
}