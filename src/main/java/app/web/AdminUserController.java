package app.web;


import app.user.model.User;
import app.user.model.UserRole;
import app.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView listAllUsers() {
        List<User> users = userService.getAllUsers();
        ModelAndView mav = new ModelAndView("admin-users");
        mav.addObject("users", users);
        mav.addObject("roles", UserRole.values());
        return mav;
    }

    @PostMapping("/change-role")
    public String changeUserRole(@RequestParam UUID id, @RequestParam("role") UserRole role) {
        userService.changeUserRole(id, role);
        return "redirect:/admin";
    }
}
