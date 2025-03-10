package app.web;

import app.car.model.Car;
import app.car.service.CarService;
import app.transaction.service.TransactionService;
import app.user.model.User;
import app.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/shop")
public class ShopController {


    private final CarService carService;
    private final UserService userService;
    private final TransactionService transactionService;

    @Autowired
    public ShopController(CarService carService, UserService userService, TransactionService transactionService) {
        this.carService = carService;
        this.userService = userService;
        this.transactionService = transactionService;
    }

    @GetMapping
    public ModelAndView getShopPage(HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return new ModelAndView("redirect:/login");
        }

        List<Car> allCars = carService.getAllCars();

        List<UUID> userCarIds = user.getCars() != null
                ? user.getCars().stream().map(Car::getId).collect(Collectors.toList())
                : List.of();

        List<Car> availableCars = allCars.stream()
                .filter(car -> !userCarIds.contains(car.getId()))
                .collect(Collectors.toList());

        ModelAndView modelAndView = new ModelAndView("shop");
        modelAndView.addObject("cars", availableCars);

        return modelAndView;
    }

    @PostMapping("/buy/{carId}")
    public String buyCar(@PathVariable UUID carId, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }
        carService.buyCar(carId, user);
        User updatedUser = userService.getById(user.getId());
        updatedUser.setCars(carService.getAllCarsForUser(updatedUser));
        updatedUser.setTransactions(transactionService.getTransactionsForUser(updatedUser));
        session.setAttribute("user", updatedUser);

        return "redirect:/shop";
    }

}
