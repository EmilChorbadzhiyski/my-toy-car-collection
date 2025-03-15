package app.web;

import app.car.service.CarService;
import app.security.AuthenticationMetadata;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.CreateCarRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("user")
public class CarController {

    private final CarService carService;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(CarController.class);

    @Autowired
    public CarController(CarService carService, UserService userService) {
        this.carService = carService;
        this.userService = userService;
    }

    @GetMapping("/new")
    public ModelAndView getNewCarForm(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        User user = userService.getById(authenticationMetadata.getUserId());

        if (user == null) {
            return new ModelAndView("redirect:/login");
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("add-car");
        modelAndView.addObject("user", user);
        modelAndView.addObject("createCarRequest", new CreateCarRequest());

        return modelAndView;
    }

    @PostMapping("/new")
    public String createNewCar(
            @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata,
            @Valid CreateCarRequest createCarRequest,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "add-car";
        }

        User user = userService.getById(authenticationMetadata.getUserId());
        carService.createNewCar(createCarRequest, user);

        return "redirect:/user";
    }

    @DeleteMapping("/car/{id}/delete")
    public String deleteCar(
            @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata,
            @PathVariable UUID id) {

        carService.deleteById(id);

        return "redirect:/user";
    }
}
