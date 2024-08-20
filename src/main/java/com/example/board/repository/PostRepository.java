package com.example.board.repository;

import com.example.board.domain.Post;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//임시 저장소
@Repository
public class PostRepository {
    private static final Map<Long, Post> store = new HashMap<>();
    private static long sequence = 0L;

    public Post save(Post post) {
        post.setId(++sequence);
        store.put(post.getId(), post);
        return post;
    }

    public Post findById(Long id) {
        return store.get(id);
    }

    public List<Post> findAll() {
        return new ArrayList<>(store.values());
    }

    public void update(Long postId, Post updatePost) {
        Post post = findById(postId);
        post.setDate(updatePost.getDate());
        post.setTitle(updatePost.getTitle());
        post.setWriter(updatePost.getWriter());
        post.setContent(updatePost.getContent());
    }

    public void deleteById(Long id) {
        store.remove(id);
    }

    public void clearStore() {
        store.clear();
    }
}
