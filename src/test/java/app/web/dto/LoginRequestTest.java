package app.web.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginRequestTest {

    @Test
    void buildLoginRequest_shouldSetAllFieldsCorrectly() {
        String username = "Emil";
        String password = "123456";

        LoginRequest request = LoginRequest.builder()
                .username(username)
                .password(password)
                .build();

        assertEquals(username, request.getUsername());
        assertEquals(password, request.getPassword());
    }

    @Test
    void createLoginRequest_withNoArgsConstructor_shouldHaveNullFields() {
        LoginRequest request = new LoginRequest();

        assertEquals(null, request.getUsername());
        assertEquals(null, request.getPassword());
    }

    @Test
    void createLoginRequest_withAllArgsConstructor_shouldSetAllFields() {
        String username = "Emil";
        String password = "123456";

        LoginRequest request = new LoginRequest(username, password);

        assertEquals(username, request.getUsername());
        assertEquals(password, request.getPassword());
    }

    @Test
    void setFields_usingSetterMethods_shouldSetFieldsCorrectly() {
        LoginRequest request = new LoginRequest();

        String username = "Emil";
        String password = "123456";

        request.setUsername(username);
        request.setPassword(password);

        assertEquals(username, request.getUsername());
        assertEquals(password, request.getPassword());
    }

    @Test
    void getFields_usingGetterMethods_shouldRetrieveFieldsCorrectly() {
        String username = "Emil";
        String password = "123456";

        LoginRequest request = LoginRequest.builder()
                .username(username)
                .password(password)
                .build();

        assertEquals(username, request.getUsername());
        assertEquals(password, request.getPassword());
    }
}