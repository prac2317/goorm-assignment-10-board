package com.example.board.domain.comment;

import com.example.board.domain.post.Post;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    private Post post;

    private boolean isDeleted = false;

    public Comment() {

    }

    public Comment(String content, Post post) {
        this.content = content;
        this.post = post;
    }

    public void update(String content) {
        this.content = content;
    }
}
