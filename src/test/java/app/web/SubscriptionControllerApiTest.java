package app.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(SubscriptionController.class)
public class SubscriptionControllerApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void getSubscriptionPage_shouldReturnSubscriptionView_whenUserIsAuthenticated() throws Exception {
        MockHttpServletRequestBuilder request = get("/subscription");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("subscription"));
    }
}
