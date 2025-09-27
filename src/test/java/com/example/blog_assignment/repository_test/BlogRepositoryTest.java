package com.example.blog_assignment.repository_test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.example.blog_assignment.entity.Blog;
import com.example.blog_assignment.repository.BlogRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BlogRepositoryTest {

    @Container
    static final PostgreSQLContainer<?> postgreContainer = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Autowired
    BlogRepository blogRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreContainer::getUsername);
        registry.add("spring.datasource.password", postgreContainer::getPassword);
    }

    @BeforeEach
    void setUp() {
        Blog blog1 = new Blog();
        blog1.setTitle("Blog 1");
        blog1.setContent("Something about blog one");

        Blog blog2 = new Blog();
        blog2.setTitle("Blog 2");
        blog2.setContent("Something about blog two");

        blogRepository.save(blog1);
        blogRepository.save(blog2);
    }

    @Test
    public void testGetAllBlogs_shouldReturnListOfSizeTwo() {
        List<Blog> blogs = blogRepository.findAll();
        assertNotNull(blogs);
        assertFalse(blogs.isEmpty());
        assertEquals(2, blogs.size());
    }

    @Test
    public void testFindByTitle_ShouldReturnBlogWithSameTitle() {
        // given
        String title = "Blog 1";
        // when
        Blog blog = blogRepository.findByTitle(title);
        // then
        assertEquals(blog.getTitle(), "Blog 1");
        assertEquals(blog.getContent(), "Something about blog one");
    }

}
