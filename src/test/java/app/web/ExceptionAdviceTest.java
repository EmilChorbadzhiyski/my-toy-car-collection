package app.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@WebMvcTest(ExceptionAdvice.class)
class ExceptionAdviceTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void handleNotFoundExceptions_shouldReturnNotFoundPage() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/non-existent-page"));
        result
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.view().name("not-found"));
    }
}
