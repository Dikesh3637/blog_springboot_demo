package com.example.blog_assignment.dto;

public class UpdateBlogDto {
    String id;
    String title;
    String content;

    public UpdateBlogDto() {
    }

    public UpdateBlogDto(String id, String title, String content) {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
