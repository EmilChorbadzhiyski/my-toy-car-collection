package app.web;

import app.accessory.service.AccessoryClientService;
import app.security.AuthenticationMetadata;
import app.user.model.User;
import app.user.model.UserRole;
import app.user.service.UserService;
import app.web.dto.AccessoryResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccessoryWebController.class)
public class AccessoryControllerApiTest {

    @MockitoBean
    private AccessoryClientService accessoryClientService;

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAccessoriesPage_returnsAccessoriesView() throws Exception {
        UUID userId = UUID.randomUUID();
        AuthenticationMetadata principal = new AuthenticationMetadata(userId, "User123", "123123", UserRole.USER);
        User user = new User();
        user.setId(userId);
        when(userService.getById(userId)).thenReturn(user);
        MockHttpServletRequestBuilder request = get("/accessories").with(user(principal));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("accessories"));
    }

    @Test
    void showAddAccessoryForm_returnsAddAccessoryView() throws Exception {
        UUID userId = UUID.randomUUID();
        AuthenticationMetadata principal = new AuthenticationMetadata(userId, "User123", "123123", UserRole.USER);
        User user = new User();
        user.setId(userId);

        when(userService.getById(userId)).thenReturn(user);

        mockMvc.perform(get("/accessories/add").with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(view().name("add-accessory"))
                .andExpect(model().attributeExists("accessoryRequest"));
    }
}
