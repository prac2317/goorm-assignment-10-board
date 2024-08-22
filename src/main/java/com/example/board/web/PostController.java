package com.example.board.web;

import com.example.board.domain.post.Post;
import com.example.board.domain.post.PostRepository;
import com.example.board.web.dto.PostReadDto;
import com.example.board.web.dto.PostSaveDto;
import com.example.board.web.dto.PostUpdateDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
        model.addAttribute("postSaveDto", new PostSaveDto());
        return "post/createForm";
    }

    @PostMapping("/create")
    public String createPost(@ModelAttribute PostSaveDto postSaveDto) {
        postRepository.save(new Post(postSaveDto.getTitle(), postSaveDto.getContent()));
        return "redirect:/post";
    }

//    [--------게시글 조회-------]
    @GetMapping("/{postId}")
    public String getPost(@PathVariable("postId") Long id, Model model) {
        Optional<Post> p = postRepository.findById(id);
        if (p.isPresent()) {
            PostReadDto postReadDto = new PostReadDto(p.get().getTitle(), p.get().getContent());
            model.addAttribute("postReadDto", postReadDto);
        }
        return "post/post";
    }


//    [--------게시글 수정-------]
    @GetMapping("/{postId}/update")
    public String getUpdateForm(@PathVariable("postId") Long id, Model model) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            PostUpdateDto postUpdateDto = new PostUpdateDto(post.getTitle(), post.getContent());
            model.addAttribute("postUpdateDto", postUpdateDto);
        }
        return "post/updateForm";
    }

    @PostMapping("/{postId}/update")
    public String updatePost(@PathVariable("postId") Long id, @ModelAttribute PostUpdateDto postUpdateDto, Model model) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();

            post.update(postUpdateDto.getTitle(), postUpdateDto.getContent());
            postRepository.save(post);
        }
        return "redirect:/post";
    }

//    [--------게시글 삭제-------]
    @PostMapping("/{postId}/delete")
    public String deletePost(@PathVariable("postId") Long id) {
        postRepository.deleteById(id);
        return "redirect:/post";
    }


    //테스트용
    @PostConstruct
    public void init() {
        postRepository.deleteAll();
        postRepository.save(new Post("lsj", "hello world"));
        postRepository.save(new Post("sj", "hi world"));
        postRepository.save(new Post("jsl", "bye world"));
    }
}
