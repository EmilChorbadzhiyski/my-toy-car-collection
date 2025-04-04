package app.web;

import app.car.model.CarOrigin;
import app.car.service.CarService;
import app.security.AuthenticationMetadata;
import app.user.model.User;
import app.user.model.UserRole;
import app.user.service.UserService;
import app.web.dto.CreateCarRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarController.class)
public class CarControllerApiTest {

    @MockitoBean
    private CarService carService;

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    private User createTestUser() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("testUser" + System.currentTimeMillis());
        user.setRole(UserRole.USER);
        return user;
    }

    private CreateCarRequest createCarRequest() {
        CreateCarRequest req = new CreateCarRequest();
        req.setType("Sedan");
        req.setBrand("Toyota");
        req.setModel("Camry");
        req.setYear("2023");
        req.setNumberOfDoors(4);
        req.setCountryOfOrigin(CarOrigin.JAPAN);
        req.setColor("Silver");
        req.setPictureUrl("http://example.com/image.jpg");
        req.setPrice(BigDecimal.valueOf(10000));
        return req;
    }

    @Test
    void getNewCarForm_returnsAddCarView() throws Exception {
        UUID userId = UUID.randomUUID();
        AuthenticationMetadata principal = new AuthenticationMetadata(userId, "User123", "123123", UserRole.USER);
        User user = new User();
        user.setId(userId);

        when(userService.getById(userId)).thenReturn(user);

        MockHttpServletRequestBuilder request = get("/user/new").with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("add-car"))
                .andExpect(model().attributeExists("createCarRequest"));
    }

    @Test
    void createNewCar_validInput_redirects() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = createTestUser();
        CreateCarRequest req = createCarRequest();

        when(userService.getById(userId)).thenReturn(user);
        doNothing().when(carService).createNewCar(any(), any());

        MockHttpServletRequestBuilder postReq = org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/user/new")
                .with(user(new AuthenticationMetadata(userId, "testUser", "pw", UserRole.USER)))
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("type", req.getType())
                .param("brand", req.getBrand())
                .param("model", req.getModel())
                .param("year", String.valueOf(req.getYear()))
                .param("numberOfDoors", String.valueOf(req.getNumberOfDoors()))
                .param("countryOfOrigin", req.getCountryOfOrigin().toString())
                .param("color", req.getColor())
                .param("pictureUrl", req.getPictureUrl())
                .param("price", String.valueOf(req.getPrice()));

        mockMvc.perform(postReq)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user"));

        verify(userService).getById(userId);
        verify(carService).createNewCar(req, user);
    }

    @Test
    void deleteCar_validRequest_redirectsToUserPage() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID carIdToDelete = UUID.randomUUID();
        AuthenticationMetadata principal = new AuthenticationMetadata(userId, "testUser", "password", UserRole.USER);

        MockHttpServletRequestBuilder deleteReq = delete("/user/car/" + carIdToDelete + "/delete")
                .with(user(principal))
                .with(csrf());

        mockMvc.perform(deleteReq)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user"));

        verify(carService).deleteById(carIdToDelete);
    }
}