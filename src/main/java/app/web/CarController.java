package app.web;

import app.car.service.CarService;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.CreateCarRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ModelAndView getNewCarForm(HttpSession session) {
        User user = (User) session.getAttribute("user");

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
    public String createNewCar(@Valid CreateCarRequest createCarRequest, BindingResult bindingResult, HttpSession session) {

        if (bindingResult.hasErrors()) {
            return "add-car";
        }
        User userFromSession = (User) session.getAttribute("user");
        User managedUser = userService.getById(userFromSession.getId());
        carService.createNewCar(createCarRequest, managedUser);
        managedUser.setCars(carService.getAllCarsForUser(managedUser));
        session.setAttribute("user", managedUser);

        return "redirect:/user";
    }

    @DeleteMapping("/car/{id}/delete")
    public String deleteCar(@PathVariable UUID id, HttpSession session) {
        carService.deleteById(id);

        User userFromSession = (User) session.getAttribute("user");
        User user = userService.getById(userFromSession.getId());
        user.setCars(carService.getAllCarsForUser(user));
        session.setAttribute("user", user);
        
        return "redirect:/user";
    }
}
