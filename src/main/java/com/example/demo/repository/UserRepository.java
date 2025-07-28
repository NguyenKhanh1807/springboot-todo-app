package com.example.demo.repository;

import com.example.demo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional; // Thêm import này

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    // THÊM PHƯƠNG THỨC NÀY:
    // Tìm kiếm User bằng email.
    // Dùng Optional để xử lý trường hợp không tìm thấy.
    Optional<UserEntity> findByEmail(String email);
}