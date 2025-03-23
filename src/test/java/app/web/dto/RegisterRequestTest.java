package app.web.dto;

import app.user.model.Country;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegisterRequestTest {

    @Test
    void buildRegisterRequest_shouldSetAllFieldsCorrectly() {
        String username = "emil";
        String password = "123456";
        Country country = Country.BULGARIA;

        RegisterRequest request = RegisterRequest.builder()
                .username(username)
                .password(password)
                .country(country)
                .build();

        assertEquals(username, request.getUsername());
        assertEquals(password, request.getPassword());
        assertEquals(country, request.getCountry());
    }

    @Test
    void createRegisterRequest_withNoArgsConstructor_shouldHaveNullFields() {
        RegisterRequest request = new RegisterRequest();

        assertEquals(null, request.getUsername());
        assertEquals(null, request.getPassword());
        assertEquals(null, request.getCountry());
    }

    @Test
    void createRegisterRequest_withAllArgsConstructor_shouldSetAllFields() {
        String username = "emil";
        String password = "123456";
        Country country = Country.BULGARIA;

        RegisterRequest request = new RegisterRequest(username, password, country);

        assertEquals(username, request.getUsername());
        assertEquals(password, request.getPassword());
        assertEquals(country, request.getCountry());
    }

    @Test
    void setFields_usingSetterMethods_shouldSetFieldsCorrectly() {
        RegisterRequest request = new RegisterRequest();

        String username = "emil";
        String password = "123456";
        Country country = Country.GERMANY;

        request.setUsername(username);
        request.setPassword(password);
        request.setCountry(country);

        assertEquals(username, request.getUsername());
        assertEquals(password, request.getPassword());
        assertEquals(country, request.getCountry());
    }

    @Test
    void getFields_usingGetterMethods_shouldRetrieveFieldsCorrectly() {
        String username = "emil";
        String password = "123456";
        Country country = Country.FRANCE;

        RegisterRequest request = RegisterRequest.builder()
                .username(username)
                .password(password)
                .country(country)
                .build();

        assertEquals(username, request.getUsername());
        assertEquals(password, request.getPassword());
        assertEquals(country, request.getCountry());
    }
}