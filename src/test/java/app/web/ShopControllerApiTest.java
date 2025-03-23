package app.web;

import app.car.model.Car;
import app.car.service.CarService;
import app.security.AuthenticationMetadata;
import app.transaction.service.TransactionService;
import app.user.model.User;
import app.user.model.UserRole;
import app.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ShopController.class)
public class ShopControllerApiTest {

    @MockitoBean
    private  CarService carService;
    @MockitoBean
    private  UserService userService;
    @MockitoBean
    private  TransactionService transactionService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getShopPage_returnsShopView() throws Exception {
        UUID userId = UUID.randomUUID();
        AuthenticationMetadata principal = new AuthenticationMetadata(userId, "User123", "123123", UserRole.USER);
        User user = new User();
        user.setId(userId);
        List<Car> cars = List.of();
        when(userService.getById(userId)).thenReturn(user);
        when(carService.getAllCars()).thenReturn(cars);
        MockHttpServletRequestBuilder request = get("/shop").with(user(principal));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("shop"));
    }

    @Test
    void getShopPage_redirectsToLogin() throws Exception {
        UUID userId = UUID.randomUUID();
        AuthenticationMetadata principal = new AuthenticationMetadata(userId, "User123", "123123", UserRole.USER);
        when(userService.getById(userId)).thenReturn(null);
        MockHttpServletRequestBuilder request = get("/shop").with(user(principal));
        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void getShopPage_returnsShopViewWithEmptyCarList() throws Exception {
        UUID userId = UUID.randomUUID();
        AuthenticationMetadata principal = new AuthenticationMetadata(userId, "User123", "123123", UserRole.USER);
        User user = new User();
        user.setId(userId);
        List<Car> cars = List.of();
        when(userService.getById(userId)).thenReturn(user);
        when(carService.getAllCars()).thenReturn(cars);
        MockHttpServletRequestBuilder request = get("/shop").with(user(principal));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("shop"))
                .andExpect(model().attribute("cars", cars));
    }
}
