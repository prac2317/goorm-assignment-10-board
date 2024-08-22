package com.example.board.web;

import com.example.board.domain.comment.Comment;
import com.example.board.domain.comment.CommentRepository;
import com.example.board.domain.post.Post;
import com.example.board.domain.post.PostRepository;
import com.example.board.web.dto.CommentDto;
import com.example.board.web.dto.PostReadDto;
import com.example.board.web.dto.PostSaveDto;
import com.example.board.web.dto.PostUpdateDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

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

//    [--------게시글과 댓글 조회-------]
    @GetMapping("/{postId}")
    public String getPost(@PathVariable("postId") Long id, Model model) {
        Optional<Post> p = postRepository.findById(id);
        if (p.isPresent()) {
            PostReadDto postReadDto = new PostReadDto(p.get().getTitle(), p.get().getContent());
            model.addAttribute("postReadDto", postReadDto);

//        TODO 스트림? 활용해서 '해당 게시글을 post로 갖고 있는' 댓글만 리스트로 만들기
            List<Comment> comments = commentRepository.findAll().stream().filter(c -> c.getPost().equals(p.get())).toList();

            //TODO 리스트 모델로 저장
            model.addAttribute("comments", comments);

            //임시 TODO commentDto 모델로 저장
            model.addAttribute("commentDto", new CommentDto());
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


//   TODO [--------댓글 추가-------]
    /**
     * 댓글 dto 받기
     * 게시글 id 받아오기
     * 댓글 저장소에 save
     * 게시글 리다이렉트(모델에 게시글 넣기?)
     */
    @PostMapping("/{postId}/comment/create")
    public String createComment(@PathVariable("postId") Long id, @ModelAttribute CommentDto commentDto, RedirectAttributes redirectAttributes) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            commentRepository.save(new Comment(commentDto.getContent(), post));
            // TODO 게시글 상세 페이지로 리다이렉트 시키기
        }
        return "redirect:/post";
    }

//   TODO [--------댓글 삭제-------]
    /**
     * 댓글 id 받아오기
     * 댓글 저장소에서 삭제
     * 게시글 리다이렉트(모델에 게시글 넣기?)
     */
    @PostMapping("/{postId}/comment/{commentId}/delete")
    public String deleteComment(@PathVariable("postId") Long postId, @PathVariable("commentId") Long commentId) {
        commentRepository.deleteById(commentId);
        return "redirect:/post";
    }

//   TODO [--------댓글 수정-------]
    /**
     * 댓글 id 받아오기
     * 댓글 저장소에서 수정
     * 게시글 리다이렉트(모델에 게시글 넣기?)
     */
    @PostMapping("/{postId}/comment/{commentId}/update")
    public String updateComment(@PathVariable("postId") Long postId, @PathVariable("commentId") Long commentId, @ModelAttribute CommentDto commentDto) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();

            comment.update( commentDto.getContent());
            commentRepository.save(comment);
        }
        return "redirect:/post";
    }


    //테스트용
    @PostConstruct
    public void init() {
        postRepository.deleteAll();
        Post post1 = postRepository.save(new Post("post1", "hello world"));
        Post post2 = postRepository.save(new Post("post2", "hi world"));
        Post post3 = postRepository.save(new Post("post3", "bye world"));

        commentRepository.deleteAll();
        commentRepository.save(new Comment("동의합니다", post1));
        commentRepository.save(new Comment("저도 동의합니다", post1));
        commentRepository.save(new Comment("저는 동의하지 않습니다", post1));

        commentRepository.save(new Comment("재밌습니다", post2));
    }
}
