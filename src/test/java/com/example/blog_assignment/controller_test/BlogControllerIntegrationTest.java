package com.example.blog_assignment.controller_test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.example.blog_assignment.dto.AddBlogDto;
import com.example.blog_assignment.dto.UpdateBlogDto;
import com.example.blog_assignment.entity.Blog;
import com.example.blog_assignment.repository.BlogRepository;

@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BlogControllerIntegrationTest {

	@Container
	static final PostgreSQLContainer<?> postgreContainer = new PostgreSQLContainer<>("postgres:17")
			.withDatabaseName("testdb")
			.withUsername("testuser")
			.withPassword("testpass");

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgreContainer::getJdbcUrl);
		registry.add("spring.datasource.username", postgreContainer::getUsername);
		registry.add("spring.datasource.password", postgreContainer::getPassword);
	}

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private BlogRepository blogRepository;

	@BeforeEach
	void setUp() {
		blogRepository.deleteAll();
	}

	@Test
	public void canEstablishConnection() {
		assertTrue(postgreContainer.isCreated());
		assertTrue(postgreContainer.isRunning());
	}

	@Test
	public void testGetAllBlogs() {
		ResponseEntity<Blog[]> response = restTemplate.getForEntity("/api/blogs/", Blog[].class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
	}

	@Test
	public void testCreateBlog() {
		AddBlogDto dto = new AddBlogDto();
		dto.setTitle("Test Blog");
		dto.setContent("Test Content");

		ResponseEntity<Blog> response = restTemplate.postForEntity("/api/blogs/", dto, Blog.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("Test Blog", response.getBody().getTitle());
	}

	@Test
	public void testGetBlogById() {
		// Create a blog first
		Blog blog = new Blog();
		blog.setTitle("Sample Blog");
		blog.setContent("Sample Content");
		Blog saved = blogRepository.save(blog);

		// Get by ID
		ResponseEntity<Blog> response = restTemplate.getForEntity(
				"/api/blogs/id/" + saved.getId(),
				Blog.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Sample Blog", response.getBody().getTitle());
	}

	@Test
	public void testGetBlogByTitle() {
		// Create a blog first
		Blog blog = new Blog();
		blog.setTitle("Unique Title");
		blog.setContent("Content");
		blogRepository.save(blog);

		// Get by title
		ResponseEntity<Blog> response = restTemplate.getForEntity(
				"/api/blogs/title?title=Unique Title",
				Blog.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Unique Title", response.getBody().getTitle());
	}

	@Test
	public void testUpdateBlog() {
		// Create a blog first
		Blog blog = new Blog();
		blog.setTitle("Original");
		blog.setContent("Original Content");
		Blog saved = blogRepository.save(blog);

		// Update it
		UpdateBlogDto dto = new UpdateBlogDto();
		dto.setId(saved.getId().toString());
		dto.setTitle("Updated");
		dto.setContent("Updated Content");

		HttpEntity<UpdateBlogDto> request = new HttpEntity<>(dto);
		ResponseEntity<Blog> response = restTemplate.exchange(
				"/api/blogs/",
				HttpMethod.PUT,
				request,
				Blog.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Updated", response.getBody().getTitle());
	}

	@Test
	public void testCreateMultipleBlogs() {
		AddBlogDto dto1 = new AddBlogDto();
		dto1.setTitle("Blog 1");
		dto1.setContent("Content 1");
		restTemplate.postForEntity("/api/blogs/", dto1, Blog.class);

		AddBlogDto dto2 = new AddBlogDto();
		dto2.setTitle("Blog 2");
		dto2.setContent("Content 2");
		restTemplate.postForEntity("/api/blogs/", dto2, Blog.class);

		ResponseEntity<Blog[]> response = restTemplate.getForEntity("/api/blogs/", Blog[].class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, response.getBody().length);
	}

}
