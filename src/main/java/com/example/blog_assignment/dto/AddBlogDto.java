package com.example.blog_assignment.dto;

public class AddBlogDto {
    private String title;
    private String content;

    public AddBlogDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public AddBlogDto() {
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
