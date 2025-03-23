package app.web.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateNewPostTest {

    @Test
    void buildCreateNewPost_shouldSetAllFieldsCorrectly() {
        String title = "Test Title";
        String description = "Test Description";

        CreateNewPost request = CreateNewPost.builder()
                .title(title)
                .description(description)
                .build();

        assertEquals(title, request.getTitle());
        assertEquals(description, request.getDescription());
    }

    @Test
    void createCreateNewPost_withNoArgsConstructor_shouldHaveNullFields() {
        CreateNewPost request = new CreateNewPost();

        assertEquals(null, request.getTitle());
        assertEquals(null, request.getDescription());
    }

    @Test
    void createCreateNewPost_withAllArgsConstructor_shouldSetAllFields() {
        String title = "Another Title";
        String description = "Another Description";

        CreateNewPost request = new CreateNewPost(title, description);

        assertEquals(title, request.getTitle());
        assertEquals(description, request.getDescription());
    }

    @Test
    void setFields_usingSetterMethods_shouldSetFieldsCorrectly() {
        CreateNewPost request = new CreateNewPost();

        String title = "Updated Title";
        String description = "Updated Description";

        request.setTitle(title);
        request.setDescription(description);

        assertEquals(title, request.getTitle());
        assertEquals(description, request.getDescription());
    }

    @Test
    void getFields_usingGetterMethods_shouldRetrieveFieldsCorrectly() {
        String title = "Retrieved Title";
        String description = "Retrieved Description";

        CreateNewPost request = CreateNewPost.builder()
                .title(title)
                .description(description)
                .build();

        assertEquals(title, request.getTitle());
        assertEquals(description, request.getDescription());
    }
}