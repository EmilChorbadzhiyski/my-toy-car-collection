package app.post;

import app.post.model.Post;
import app.post.repository.PostRepository;
import app.post.service.PostService;
import app.user.model.User;
import app.web.dto.CreateNewPost;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceUTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Test
    void whenUserCreatePostShouldSavePost() {
        CreateNewPost createNewPost = new CreateNewPost();
        User user = new User();
        user.setId(UUID.randomUUID());

        postService.create(createNewPost, user);

        verify(postRepository).save(argThat(post ->
                user.equals(post.getOwner()) &&
                        LocalDate.now().equals(post.getAddedOn()) &&
                        LocalDate.now().equals(post.getDate()) &&
                        !post.isVisible()
        ));
    }

    @Test
    void deletePostMethod_shouldDeletePost() {
        UUID postId = UUID.randomUUID();
        postService.deletePostById(postId);
        verify(postRepository, times(1)).deleteById(postId);
    }

    @Test
    void shouldSetVisibilityWhenChangeAndSave() {
        UUID postId = UUID.randomUUID();
        boolean newVisibility = true;

        Post existingPost = Post.builder()
                .id(postId)
                .title("Test Post")
                .description("Test Description")
                .addedOn(LocalDate.now())
                .date(LocalDate.now())
                .isVisible(false)
                .build();

        when(postRepository.findById(postId)).thenReturn(Optional.of(existingPost));
        postService.setPostVisibility(postId, newVisibility);
        verify(postRepository, times(1)).save(argThat(post ->
                post.getId().equals(postId) &&
                        post.isVisible() == newVisibility
        ));
    }

    @Test
    void shouldThrowExceptionIfPostNotFound() {
        UUID postId = UUID.randomUUID();
        boolean newVisibility = true;
        when(postRepository.findById(postId)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> postService.setPostVisibility(postId, newVisibility));
        verify(postRepository, never()).save(any(Post.class));
    }

    @Test
    void shouldReturnListOfVisiblePosts() {
        Post post1 = Post.builder()
                .id(UUID.randomUUID())
                .title("Visible Post 1")
                .isVisible(true)
                .build();

        Post post2 = Post.builder()
                .id(UUID.randomUUID())
                .title("Visible Post 2")
                .isVisible(true)
                .build();

        List<Post> visiblePosts = Arrays.asList(post1, post2);
        when(postRepository.findByIsVisible(true)).thenReturn(visiblePosts);
        List<Post> result = postService.getAllVisiblePosts();
        assertEquals(visiblePosts, result);
        verify(postRepository, times(1)).findByIsVisible(true);
    }

    @Test
    void whenAllVisiblePostIsEmpty_shouldReturnEmptyListIfNoVisiblePosts() {
        when(postRepository.findByIsVisible(true)).thenReturn(List.of());
        List<Post> result = postService.getAllVisiblePosts();
        assertTrue(result.isEmpty());
        verify(postRepository, times(1)).findByIsVisible(true);
    }

    @Test
    void shouldReturnPostWhenFound() {
        UUID postId = UUID.randomUUID();
        Post expectedPost = Post.builder()
                .id(postId)
                .title("Post")
                .description("Description")
                .addedOn(LocalDate.now())
                .date(LocalDate.now())
                .isVisible(true)
                .build();

        when(postRepository.findById(postId)).thenReturn(Optional.of(expectedPost));
        Post actualPost = postService.findById(postId);
        assertEquals(expectedPost, actualPost);
        verify(postRepository, times(1)).findById(postId);
    }

    @Test
    void whenPostNotFound_ShouldThrowException() {
        UUID postId = UUID.randomUUID();
        when(postRepository.findById(postId)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> postService.findById(postId));
        verify(postRepository, times(1)).findById(postId);
    }

    @Test
    void shouldReturnAllPostsForUser() {
        User user = mock(User.class);
        Post post1 = mock(Post.class);
        Post post2 = mock(Post.class);
        List<Post> posts = Arrays.asList(post1, post2);
        when(postRepository.findByOwner(user)).thenReturn(posts);
        List<Post> result = postService.getAllPostsForUser(user);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(posts, result);
        verify(postRepository, times(1)).findByOwner(user);
    }



}
