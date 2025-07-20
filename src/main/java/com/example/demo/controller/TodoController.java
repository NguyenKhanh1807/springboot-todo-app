package com.example.demo.controller;

import com.example.demo.dto.TodoDto;
import com.example.demo.service.TodoService;
import com.example.demo.payload.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/todos") // Tất cả các API trong class này sẽ có tiền tố /api/todos
public class TodoController {

    private final TodoService todoService;

    // Dependency Injection
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    // API để lấy danh sách tất cả công việc
    @GetMapping
    public ResponseEntity<ApiResponse<List<TodoDto>>> getAllTodos() {
        List<TodoDto> todos = todoService.getAllTodos();
        ApiResponse<List<TodoDto>> response = new ApiResponse<>(200, "Lấy danh sách thành công", todos);
        return ResponseEntity.ok(response);
    }

    // Lấy danh sách Todo theo userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getTodosByUser(@PathVariable Integer userId) {
        List<TodoDto> todos = todoService.getTodosByUserId(userId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Lấy TODO theo User thành công", todos));
    }

    // API để tạo một công việc mới
    @PostMapping
    public ResponseEntity<?> createTodo(@Valid @RequestBody TodoDto dto, BindingResult result) {

        if (result.hasErrors()) {
            // Nếu có lỗi, gom tất cả message lại thành 1 danh sách
            List<String> errors = result.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .toList();
            return ResponseEntity.badRequest().body(errors);
        }

        TodoDto newTodo = todoService.createTodo(dto);
        return ResponseEntity.ok(newTodo);
    }
}