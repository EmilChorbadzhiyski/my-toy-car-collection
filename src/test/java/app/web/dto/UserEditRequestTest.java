package app.web.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserEditRequestTest {

    @Test
    void buildUserEditRequest_shouldSetAllFieldsCorrectly() {
        String firstName = "emil";
        String lastName = "Chorbadzhiyski";
        String email = "emil@example.com";
        String profilePicture = "pic.jpg";

        UserEditRequest request = UserEditRequest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .profilePicture(profilePicture)
                .build();

        assertEquals(firstName, request.getFirstName());
        assertEquals(lastName, request.getLastName());
        assertEquals(email, request.getEmail());
        assertEquals(profilePicture, request.getProfilePicture());
    }

    @Test
    void createEmptyUserEditRequest_shouldHaveNullFields() {
        UserEditRequest request = UserEditRequest.builder().build();

        assertEquals(null, request.getFirstName());
        assertEquals(null, request.getLastName());
        assertEquals(null, request.getEmail());
        assertEquals(null, request.getProfilePicture());
    }

    @Test
    void setFields_usingSetterMethods_shouldSetFieldsCorrectly() {
        UserEditRequest request = UserEditRequest.builder().build();

        String firstName = "emil";
        String lastName = "Chorbadzhiyski";
        String email = "emil@example.com";
        String profilePicture = "pic.jpg.png";

        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setEmail(email);
        request.setProfilePicture(profilePicture);

        assertEquals(firstName, request.getFirstName());
        assertEquals(lastName, request.getLastName());
        assertEquals(email, request.getEmail());
        assertEquals(profilePicture, request.getProfilePicture());
    }

    @Test
    void getFields_usingGetterMethods_shouldRetrieveFieldsCorrectly() {
        String firstName = "emil";
        String lastName = "Chorbadzhiyski";
        String email = "emil@example.com";
        String profilePicture = "pic.jpg.gif";

        UserEditRequest request = UserEditRequest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .profilePicture(profilePicture)
                .build();

        assertEquals(firstName, request.getFirstName());
        assertEquals(lastName, request.getLastName());
        assertEquals(email, request.getEmail());
        assertEquals(profilePicture, request.getProfilePicture());
    }
}