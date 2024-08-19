package com.example.board.domain;

import java.util.List;

public class Post {

    private long id;
    private String date;
    private String title;
    private String content;
    private List<Comment> comments;
}
