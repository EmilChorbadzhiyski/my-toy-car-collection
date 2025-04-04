package app.web;

import app.security.AuthenticationMetadata;
import app.user.model.User;
import app.user.model.UserRole;
import app.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerApiTest {

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    private User createTestUser(UUID id) {
        User user = new User();
        user.setId(id);
        user.setUsername("testUser" + System.currentTimeMillis());
        user.setRole(UserRole.USER);
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail("test@example.com");
        return user;
    }

    @Test
    void getUserProfileEditPage_authenticatedUser_returnsEditProfileView() throws Exception {
        UUID userId = UUID.randomUUID();
        AuthenticationMetadata principal = new AuthenticationMetadata(userId, "testUser", "password", UserRole.USER);
        User user = createTestUser(userId);

        when(userService.getById(userId)).thenReturn(user);

        MockHttpServletRequestBuilder getRequest = get("/user/" + userId + "/edit-profile").with(user(principal));

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(view().name("edit-profile"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("userEditRequest"));
    }

    @Test
    void updateUserProfile_authenticatedUser_ownProfile_redirectsToUserPage() throws Exception {
        UUID userId = UUID.randomUUID();
        AuthenticationMetadata principal = new AuthenticationMetadata(userId, "testUser", "password", UserRole.USER);

        MockHttpServletRequestBuilder putRequest = put("/user/" + userId + "/edit-profile")
                .with(user(principal))
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("firstName", "Updated")
                .param("lastName", "User")
                .param("email", "updated@example.com");

        mockMvc.perform(putRequest)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user"));

        verify(userService).editUserDetails(eq(userId), any());
    }

    @Test
    void updateUserProfile_authenticatedUser_differentProfile_redirectsToUserPage() throws Exception {
        UUID authUserId = UUID.randomUUID();
        UUID targetUserId = UUID.randomUUID();
        AuthenticationMetadata principal = new AuthenticationMetadata(authUserId, "testUser", "password", UserRole.USER);

        MockHttpServletRequestBuilder putRequest = put("/user/" + targetUserId + "/edit-profile")
                .with(user(principal))
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("firstName", "Updated");

        mockMvc.perform(putRequest)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user"));

        verifyNoInteractions(userService);
    }

    @Test
    void putUnauthorizedRequestToSwitchRole_shouldReturn404AndNotFoundView() throws Exception {
        AuthenticationMetadata principal = new AuthenticationMetadata(UUID.randomUUID(), "User123", "123123", UserRole.USER);
        MockHttpServletRequestBuilder request = put("/users/{id}/role", UUID.randomUUID())
                .with(user(principal))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(view().name("not-found"));
    }
}