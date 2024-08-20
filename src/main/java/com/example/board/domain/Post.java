package com.example.board.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class Post {

    private long id;
    private String date;
    private String title;
    private String writer;
    private String content;
    private List<Comment> comments;

    public Post() {
    }

    public Post(String date, String title, String writer, String content) {
        this.date = date;
        this.title = title;
        this.writer = writer;
        this.content = content;
    }
}
