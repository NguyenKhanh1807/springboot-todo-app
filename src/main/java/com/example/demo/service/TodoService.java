package com.example.demo.service;

import com.example.demo.dto.TodoDto;
import com.example.demo.entity.TodoEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.mapper.TodoMapper;
import com.example.demo.repository.TodoRepository;
import com.example.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    public TodoService(TodoRepository todoRepository, UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    public List<TodoDto> getAllTodos() {
        log.info("Lấy toàn bộ danh sách TODO");
        List<TodoEntity> entities = todoRepository.findAll();
        return entities.stream()
                .map(TodoMapper::toDto)
                .collect(Collectors.toList());
    }

    public TodoDto createTodo(TodoDto dto) {
        log.info("Tạo mới TODO với title = {}", dto.getTitle());

        // Lấy UserEntity từ userId trong DTO
        UserEntity user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User không tồn tại với ID: " + dto.getUserId()));

        // Chuyển từ DTO sang Entity
        TodoEntity entity = TodoMapper.toEntity(dto);
        entity.setUser(user); // Gắn user

        // Lưu vào DB
        TodoEntity saved = todoRepository.save(entity);

        // Trả lại DTO
        return TodoMapper.toDto(saved);
    }

    public List<TodoDto> getTodosByUserId(Integer userId) {
        log.info("Lấy danh sách TODO cho userId = {}", userId);
        List<TodoEntity> entities = todoRepository.findByUserId(userId);
        return entities.stream()
                .map(TodoMapper::toDto)
                .collect(Collectors.toList());
    }
}