package com.example.blog_assignment.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.blog_assignment.entity.Blog;

@Repository
public interface BlogRepository extends JpaRepository<Blog, UUID> {

}
