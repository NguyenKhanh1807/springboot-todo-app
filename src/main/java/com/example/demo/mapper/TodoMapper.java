package com.example.demo.mapper;

import com.example.demo.dto.TodoDto;
import com.example.demo.entity.TodoEntity;

public class TodoMapper {

    public static TodoDto toDto(TodoEntity entity) {

        if (entity == null) return null;

        TodoDto dto = new TodoDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setCompleted(entity.isCompleted());

        // Lấy userId từ entity.user (nếu có)
        if (entity.getUser() != null) {
            dto.setUserId(entity.getUser().getId());
        }

        return dto;
    }

    public static TodoEntity toEntity(TodoDto dto) {

        if (dto == null) return null;

        TodoEntity entity = new TodoEntity();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setCompleted(dto.isCompleted());

        // Không gắn user ở đây – sẽ gắn riêng trong service
        return entity;
    }
}
