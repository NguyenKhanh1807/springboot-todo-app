package com.example.demo.repository;

import com.example.demo.entity.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<TodoEntity, Integer> {
    // TodoRepository.java
    List<TodoEntity> findByUserId(Integer userId);
}
