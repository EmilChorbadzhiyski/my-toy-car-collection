package app.web;

import app.security.AuthenticationMetadata;
import app.user.model.User;
import app.user.model.UserRole;
import app.user.repository.UserRepository;
import app.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AdminUserController.class)
public class AdminControllerApiTest {

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    private User createTestUser() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("testUser" + System.currentTimeMillis());
        user.setRole(UserRole.USER);
        return user;
    }

    @Test
    void listAllUsers_returnsAdminUsersView() throws Exception {
        UUID adminId = UUID.randomUUID();
        AuthenticationMetadata principal = new AuthenticationMetadata(adminId, "AdminUser", "admin123", UserRole.ADMIN);
        User user1 = new User();
        user1.setId(UUID.randomUUID());
        user1.setUsername("User1");
        User user2 = new User();
        user2.setId(UUID.randomUUID());
        user2.setUsername("User2");

        when(userService.getAllUsers()).thenReturn(List.of(user1, user2));
        MockHttpServletRequestBuilder request = get("/admin").with(user(principal));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("admin-users"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attributeExists("roles"));
    }

    @Test
    @DisplayName("Given users exist, when listing users, then return all users")
    @WithMockUser(roles = "ADMIN")
    public void givenUsersExist_whenListAllUsers_thenReturnUsers() throws Exception {
        List<User> users = Arrays.asList(createTestUser(), createTestUser());
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("users", hasSize(2)))
                .andExpect(model().attributeExists("roles"))
                .andExpect(view().name("admin-users"));

        verify(userService).getAllUsers();
    }
}
