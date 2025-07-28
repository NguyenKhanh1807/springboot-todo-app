package com.example.demo;

import com.example.demo.dto.RegisterRequest; // Import DTO for registration
import com.example.demo.dto.TodoDto;
import com.example.demo.model.Role;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.TodoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc; // For API testing
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional; // Ensure tests are clean

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc // Enable API testing capabilities
@Transactional // Ensures each test runs in its own transaction and rolls back afterward
class DemoApplicationTests {

	@Autowired
	private MockMvc mockMvc; // For making mock HTTP requests

	@Autowired
	private ObjectMapper objectMapper; // For converting objects to JSON

	@Autowired
	private TodoService todoService;

	@Autowired
	private UserRepository userRepository;

	@Test
	void contextLoads() {
		assertNotNull(todoService);
		assertNotNull(mockMvc);
	}

	// --- Existing Test (Unchanged) ---
	@Test
	void testCreateTodo() {
		// GIVEN - Create a user
		UserEntity user = new UserEntity();
		user.setFirstname("Test");
		user.setLastname("User");
		user.setEmail("testuser.createtodo@example.com"); // Use a unique email for this test
		user.setPassword("password");
		user.setRole(Role.USER);
		user = userRepository.save(user);

		String title = "Học về Integration Test";
		TodoDto dto = new TodoDto();
		dto.setTitle(title);
		dto.setCompleted(false);
		dto.setUserId(user.getId());

		// WHEN - Call the service
		TodoDto createdTodo = todoService.createTodo(dto);

		// THEN - Assert the results
		assertNotNull(createdTodo);
		assertEquals(title, createdTodo.getTitle());
		assertEquals(user.getId(), createdTodo.getUserId());
	}

	// --- NEW API Integration Test ---
	@Test
	void testRegisterApiEndpoint() throws Exception {
		// GIVEN - Registration data
		var registerRequest = RegisterRequest.builder()
				.firstname("ApiTest")
				.lastname("User")
				.email("api.test@example.com")
				.password("password123")
				.build();

		// WHEN & THEN - Perform POST request and check the response
		mockMvc.perform(post("/api/v1/auth/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(registerRequest)))
				.andExpect(status().isOk()) // Expect HTTP 200 OK
				.andExpect(jsonPath("$.code").value(1000)) // Expect our custom success code
				.andExpect(jsonPath("$.result.token").exists()); // Expect a token in the result
	}
}