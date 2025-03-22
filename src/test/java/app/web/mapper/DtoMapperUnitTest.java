package app.web.mapper;


import app.user.model.User;
import app.web.dto.UserEditRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class DtoMapperUnitTest {

    @Test
    void givenHappyPath_whenMappingUserToUserEditRequest() {
        User user = User.builder()
                .firstName("Emil")
                .lastName("Petrov")
                .email("emil_913@abv.bg")
                .profilePicture("new_picture.png")
                .build();

        UserEditRequest resultDto = DtoMapper.mapUserToUserEditRequest(user);

        assertEquals(user.getFirstName(), resultDto.getFirstName());
        assertEquals(user.getLastName(), resultDto.getLastName());
        assertEquals(user.getEmail(), resultDto.getEmail());
        assertEquals(user.getProfilePicture(), resultDto.getProfilePicture());
    }
}
