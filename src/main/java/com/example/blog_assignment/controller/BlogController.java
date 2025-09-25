package com.example.blog_assignment.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.blog_assignment.dto.AddBlogDto;
import com.example.blog_assignment.entity.Blog;
import com.example.blog_assignment.exception.BlogNotFoundException;
import com.example.blog_assignment.service.BlogService;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/blogs")
public class BlogController {

    private final BlogService blogService;

    BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Blog>> getAllBlogs() {
        List<Blog> blogs = blogService.getAllBlogs();
        return ResponseEntity.ok(blogs);
    }

    @GetMapping("/id")
    public ResponseEntity<Blog> getById(@RequestParam String id) {
        Blog blog = blogService.getById(id);
        return ResponseEntity.ok(blog);
    }

    @GetMapping("/title")
    public ResponseEntity<Blog> getByTitle(@RequestParam String title) {
        Blog blogs = blogService.getByTitle(title);
        return ResponseEntity.ok(blogs);
    }

    @PostMapping("/")
    public ResponseEntity<Blog> addBlog(@RequestBody AddBlogDto addBlogDto) {
        Blog createdBlog = blogService.addBlog(addBlogDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBlog);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Blog> updateBlog(@PathVariable String id, @RequestBody AddBlogDto dto) {
        Blog updatedBlog = blogService.updateBlog(id, dto);
        return ResponseEntity.ok(updatedBlog);
    }

}
