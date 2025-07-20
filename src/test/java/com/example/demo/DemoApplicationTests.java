package com.example.demo;

import com.example.demo.dto.TodoDto;
import com.example.demo.service.TodoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	private TodoService todoService;

	@Autowired
	private UserRepository userRepository;

	@Test
	void contextLoads() {
		assertNotNull(todoService);
	}

	@Test
	void testCreateTodo() {
		// GIVEN - Tạo một User trước
		UserEntity user = new UserEntity();
		user.setName("Test User");
		user = userRepository.save(user); // Lưu và lấy ID

		String title = "Học về Unit Test";

		TodoDto dto = new TodoDto();
		dto.setTitle(title);
		dto.setCompleted(false);
		dto.setUserId(user.getId()); // Gắn userId cho DTO

		// WHEN
		TodoDto createdTodo = todoService.createTodo(dto);

		// THEN
		assertNotNull(createdTodo);
		assertEquals(title, createdTodo.getTitle());
		assertEquals(false, createdTodo.isCompleted());
		assertEquals(user.getId(), createdTodo.getUserId()); // kiểm tra đúng user
	}
}