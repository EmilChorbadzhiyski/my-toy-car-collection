package app.web;


import app.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IndexController.class)
public class IndexControllerTest {

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void getRequestToIndexEndpoint_shouldReturnIndexView() throws Exception {
        MockHttpServletRequestBuilder request = get("/");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    @WithMockUser
    void getRequestToRegisterEndpoint_shouldReturnRegisterViewWithEmptyModel() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("registerRequest"))
                .andExpect(model().attribute("registerRequest",
                        org.hamcrest.Matchers.hasProperty("username")))
                .andExpect(model().attribute("registerRequest",
                        org.hamcrest.Matchers.hasProperty("password")))
                .andExpect(model().attribute("registerRequest",
                        org.hamcrest.Matchers.hasProperty("country")));
    }

    @Test
    @WithMockUser
    void postRegister_withValidData_shouldRedirectToLogin() throws Exception {
        mockMvc.perform(post("/register")
                        .param("username", "Vik123")
                        .param("password", "123456")
                        .param("country", "BULGARIA")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    @WithMockUser
    void shouldReturnInternalServerErrorView_WhenPostRegisterIsWithInvalidData() throws Exception {
        mockMvc.perform(post("/register")
                        .param("username", "")
                        .param("password", "123456")
                        .param("country", "BULGARIA")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("internal-server-error"));
    }

    @Test
    @WithMockUser
    void shouldReturnPrivacyPolicyView() throws Exception {
        mockMvc.perform(get("/privacy-policy"))
                .andExpect(status().isOk())
                .andExpect(view().name("privacy-policy"));
    }

    @Test
    @WithMockUser
    void shouldReturnContactsView() throws Exception {
        mockMvc.perform(get("/contacts"))
                .andExpect(status().isOk())
                .andExpect(view().name("contacts"));
    }

    @Test
    @WithMockUser
    void getRequestToRegisterEndpoint_shouldReturnRegisterView() throws Exception {
        MockHttpServletRequestBuilder request = get("/register");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("registerRequest"));
    }
}
