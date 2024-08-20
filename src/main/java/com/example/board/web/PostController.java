package com.example.board.web;

import com.example.board.domain.Post;
import com.example.board.repository.PostRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostRepository postRepository;

    @GetMapping
    public String posts(Model model) {
        List<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "post/posts";
    }


//    [--------게시글 등록-------]
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("post", new Post());
        return "post/createForm";
    }

    @PostMapping("/create")
    public String createPost(@ModelAttribute Post post, Model model) {
        postRepository.save(post);
        model.addAttribute("post", post);
        return "redirect:/post";
    }

//    [--------게시글 조회-------]
    @GetMapping("/{postId}")
    public String getPost(@PathVariable("postId") Long id, Model model) {
        Post post = postRepository.findById(id);
        model.addAttribute("post", post);
        return "post/post";
    }


//    [--------게시글 수정-------]
    @GetMapping("/{postId}/update")
    public String updateForm(@PathVariable("postId") Long id, Model model) {
        Post post = postRepository.findById(id);
        model.addAttribute("post", post);
        return "post/updateForm";
    }

    @PostMapping("/{postId}/update")
    public String updatePost(@PathVariable("postId") Long id, @ModelAttribute Post post, Model model) {
        postRepository.update(id, post);
        model.addAttribute("post", post);
        return "redirect:/post";
    }

//    [--------게시글 삭제-------]
    @PostMapping("/{postId}/delete")
    public String deletePost(@PathVariable("postId") Long id) {
        postRepository.deleteById(id);
        return "redirect:/post";
    }


    //테스트용 게시글
    @PostConstruct
    public void init() {
        postRepository.save(new Post("12", "lsj", "hello", "hello world"));
        postRepository.save(new Post("16", "sj", "hi", "hi world"));
        postRepository.save(new Post("30", "jsl", "bye", "bye world"));
    }
}
