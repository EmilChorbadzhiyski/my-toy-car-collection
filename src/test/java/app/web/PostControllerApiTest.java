package app.web;

import app.post.model.Post;
import app.post.service.PostService;
import app.security.AuthenticationMetadata;
import app.user.model.User;
import app.user.model.UserRole;
import app.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
public class PostControllerApiTest {

    @MockitoBean
    private PostService postService;
    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void getPostPage_shouldReturnPostView_whenUserIsAuthenticated() throws Exception {
        UUID userId = UUID.randomUUID();
        AuthenticationMetadata principal = new AuthenticationMetadata(userId, "User123", "123123", UserRole.USER);
        User user = new User();
        user.setId(userId);
        when(userService.getById(userId)).thenReturn(user);

        MockHttpServletRequestBuilder request = get("/post").with(user(principal));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("post"))
                .andExpect(model().attributeExists("allSharedPosts"));
    }

    @Test
    @WithMockUser
    void getNewPostPage_shouldReturnAddPostView_whenUserIsAuthenticated() throws Exception {
        UUID userId = UUID.randomUUID();
        AuthenticationMetadata principal = new AuthenticationMetadata(userId, "User123", "123123", UserRole.USER);
        User user = new User();
        user.setId(userId);
        when(userService.getById(userId)).thenReturn(user);

        MockHttpServletRequestBuilder request = get("/post/add").with(user(principal));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("add-post"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("createNewPost"));
    }

    @Test
    @WithMockUser
    void readPost_shouldReturnReadPostView_whenUserIsAuthenticated() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID postId = UUID.randomUUID();
        AuthenticationMetadata principal = new AuthenticationMetadata(userId, "User123", "123123", UserRole.USER);
        User user = new User();
        user.setId(userId);
        Post post = new Post();
        post.setId(postId);
        post.setOwner(user);
        when(userService.getById(userId)).thenReturn(user);
        when(postService.findById(postId)).thenReturn(post);
        MockHttpServletRequestBuilder request = get("/post/{id}", postId).with(user(principal));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("read-post"))
                .andExpect(model().attributeExists("post"))
                .andExpect(model().attributeExists("user"));
    }
}
