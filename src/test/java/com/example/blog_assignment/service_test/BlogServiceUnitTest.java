package com.example.blog_assignment.service_test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.blog_assignment.dto.AddBlogDto;
import com.example.blog_assignment.dto.UpdateBlogDto;
import com.example.blog_assignment.entity.Blog;
import com.example.blog_assignment.exception.BlogNotFoundException;
import com.example.blog_assignment.repository.BlogRepository;
import com.example.blog_assignment.service.BlogService;

@ExtendWith(MockitoExtension.class)
public class BlogServiceUnitTest {

	@Mock
	private BlogRepository blogRepository;

	@InjectMocks
	private BlogService blogService;

	private Blog blog1;
	private Blog blog2;

	@Captor
	ArgumentCaptor<Blog> blogArgumentCaptor;

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
		verify(blogRepository, times(1)).findByTitle(title);
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
		verify(blogRepository, times(1)).findById(UUID.fromString(uuidString));
		assertSame(blog1, resBlog);
	}

	@Test
	public void testGetById_shouldThrowExceptionWhenBlogNotFound() {
		// given
		String uuidString = "550e8400-e29b-41d4-a716-446655440003";
		UUID uuid = UUID.fromString(uuidString);
		when(blogRepository.findById(uuid)).thenReturn(Optional.empty());

		// when
		BlogNotFoundException thrown = assertThrows(BlogNotFoundException.class, () -> {
			blogService.getById(uuidString);
		});

		// then
		verify(blogRepository, times(1)).findById(uuid);
		assertEquals("Blog not found with the id : " + uuidString, thrown.getMessage());
	}

	@Test
	public void testAddBlog_shouldReturnTheAddedBlogToRepository() {
		// given
		AddBlogDto dto = new AddBlogDto("Blog 3", "content from blog three");

		// blog to be added
		Blog blogToBeAdded = new Blog();
		blogToBeAdded.setTitle(dto.getTitle());
		blogToBeAdded.setContent(dto.getContent());

		// blog returned with a UUID , when saved
		Blog blogReturned = new Blog(UUID.randomUUID(), dto.getTitle(), dto.getContent());

		when(blogRepository.save(any(Blog.class))).thenReturn(blogReturned);

		// when
		Blog resBlog = blogService.addBlog(dto);

		// then
		verify(blogRepository, times(1)).save(blogArgumentCaptor.capture());
		Blog capturedBlog = blogArgumentCaptor.getValue();

		assertEquals(dto.getTitle(), capturedBlog.getTitle());
		assertEquals(dto.getContent(), capturedBlog.getContent());
		assertNull(capturedBlog.getId());

		assertNotNull(resBlog);

		assertEquals(blogReturned.getId(), resBlog.getId());
		assertEquals(blogReturned.getTitle(), resBlog.getTitle());
		assertEquals(blogReturned.getContent(), resBlog.getContent());
		assertSame(blogReturned, resBlog);

	}

	@Test
	public void testUpdateBlog_shouldReturnTheUpdatedBlog() {
		// given
		UpdateBlogDto dto = new UpdateBlogDto();
		dto.setId(blog1.getId().toString());
		dto.setTitle("Blog 1 updated");
		dto.setContent("content from blog one updated");

		Blog updatedBlog = new Blog(blog1.getId(), dto.getTitle(), dto.getContent());

		when(blogRepository.findById(blog1.getId())).thenReturn(Optional.of(blog1));
		when(blogRepository.save(any(Blog.class))).thenReturn(updatedBlog);

		// when
		Blog returnedBlog = blogService.updateBlog(dto);

		// then
		verify(blogRepository, times(1)).findById(blog1.getId());
		assertNotNull(returnedBlog);
		assertEquals(updatedBlog.getId(), returnedBlog.getId());
		assertEquals(updatedBlog.getTitle(), returnedBlog.getTitle());
		assertEquals(updatedBlog.getContent(), returnedBlog.getContent());
		assertSame(updatedBlog, returnedBlog);
	}

	@Test
	public void testUpdateBlog_shouldThrowExceptionWhenBlogNotFound() {
		// given
		UUID randomUUID = UUID.randomUUID();
		UpdateBlogDto dto = new UpdateBlogDto();
		dto.setId(randomUUID.toString());
		dto.setContent(blog1.getContent());
		dto.setTitle(blog1.getTitle());

		when(blogRepository.findById(randomUUID)).thenReturn(Optional.empty());

		// when
		BlogNotFoundException thrown = assertThrows(BlogNotFoundException.class, () -> {
			blogService.updateBlog(dto);
		});

		// then
		verify(blogRepository, times(1)).findById(randomUUID);
		assertEquals("Blog not found with the id : " + randomUUID, thrown.getMessage());
	}

	@BeforeEach
	void setUp() {
		UUID randomUUID1 = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
		UUID randomUUID2 = UUID.fromString("550e8400-e29b-41d4-a716-446655440002");
		blog1 = new Blog(randomUUID1, "Blog 1", "content from blog one");
		blog2 = new Blog(randomUUID2, "Blog 2", "content from blog two");
	}

}
