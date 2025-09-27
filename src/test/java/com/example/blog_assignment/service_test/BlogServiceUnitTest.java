package com.example.blog_assignment.service_test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.blog_assignment.entity.Blog;
import com.example.blog_assignment.repository.BlogRepository;
import com.example.blog_assignment.service.BlogService;
import com.sun.jdi.connect.Connector.Argument;

@ExtendWith(MockitoExtension.class)
public class BlogServiceUnitTest {

	@Mock
	private BlogRepository blogRepository;

	@InjectMocks
	private BlogService blogService;

	private Blog blog1;
	private Blog blog2;

	@Captor
	private ArgumentCaptor<String> titleCaptor;

	@Captor
	private ArgumentCaptor<UUID> UuidCaptor;

	@BeforeEach
	void setUp() {
		UUID randomUUID1 = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
		UUID randomUUID2 = UUID.fromString("550e8400-e29b-41d4-a716-446655440002");
		blog1 = new Blog(randomUUID1, "Blog 1", "content from blog one");
		blog2 = new Blog(randomUUID2, "Blog 2", "content from blog two");
	}

	@Test
	public void testGetAllBlogs_shouldReturnListOfSizeTwo() {
		// given
		List<Blog> blogList = List.of(blog1, blog2);
		when(blogRepository.findAll()).thenReturn(blogList);

		// when
		List<Blog> resBlogList = blogService.getAllBlogs();

		// then
		assertNotNull(resBlogList);
		assertEquals(2, resBlogList.size());
		assertSame(blogList, resBlogList);
		verify(blogRepository, times(1)).findAll();
	}

	@Test
	public void testGetByTitle_shouldReturnBlog() {
		// give
		String title = blog1.getTitle();
		when(blogRepository.findByTitle(title)).thenReturn(blog1);

		// when
		Blog resBlog = blogService.getByTitle(title);

		// then
		verify(blogRepository, times(1)).findByTitle(titleCaptor.capture());
		String captureTitle = titleCaptor.getValue();
		assertEquals(captureTitle, title);
		assertSame(resBlog, blog1);

	}

	@Test
	public void testGetById_shouldReturnBlog() {
		// given
		String uuidString = blog1.getId().toString();
		when(blogRepository.findById(UUID.fromString(uuidString)))
				.thenReturn(Optional.of(blog1));

		// when
		Blog resBlog = blogService.getById(uuidString);

		// then
		verify(blogRepository, times(1)).findById(UuidCaptor.capture());

	}

}
