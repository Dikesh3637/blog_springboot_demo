package com.example.blog_assignment.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.blog_assignment.dto.AddBlogDto;
import com.example.blog_assignment.entity.Blog;
import com.example.blog_assignment.repository.BlogRepository;

@Service
public class BlogService {

    private final BlogRepository blogRepository;

    public BlogService(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }

    public Blog getByTitle(String title) {
        return blogRepository.findByTitle(title);
    }

    public Optional<Blog> getById(String id) {
        Optional<Blog> blogOptional = blogRepository.findById(UUID.fromString(id));
        return blogOptional;
    }

    public Blog addBlog(AddBlogDto addBlogDto) {
        Blog blog = new Blog();
        blog.setTitle(addBlogDto.getTitle());
        blog.setContent(addBlogDto.getContent());
        return blogRepository.save(blog);
    }
}
