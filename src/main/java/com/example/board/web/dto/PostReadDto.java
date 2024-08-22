package com.example.board.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostReadDto {

    private String title;
    private String content;

    public PostReadDto() {
    }

    public PostReadDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
