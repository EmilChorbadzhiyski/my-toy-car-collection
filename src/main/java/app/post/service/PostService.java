package app.post.service;

import app.post.model.Post;
import app.post.repository.PostRepository;
import app.user.model.User;
import app.web.dto.CreateNewPost;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }


    public void create(CreateNewPost createNewPost, User user) {

        Post post = Post.builder()
                .title(createNewPost.getTitle())
                .description(createNewPost.getDescription())
                .addedOn(LocalDate.now())
                .date(LocalDate.now())
                .owner(user)
                .isVisible(false)
                .build();

        postRepository.save(post);
    }

    public void deletePostById(UUID id) {
        postRepository.deleteById(id);
    }

    public void setPostVisibility(UUID id, boolean isVisible) {
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
        post.setVisible(isVisible);
        postRepository.save(post);
    }

    public List<Post> getAllVisiblePosts() {
        return postRepository.findByIsVisible(true);
    }

    public Post findById(UUID id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    public List<Post> getAllPostsForUser(User managedUser) {
        return postRepository.findByOwner(managedUser);
    }
}
