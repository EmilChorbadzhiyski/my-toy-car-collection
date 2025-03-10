package app.web;

import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.UserEditRequest;
import app.web.mapper.DtoMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView getUserProfilePage(HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return new ModelAndView("redirect:/login");
        }

        ModelAndView modelAndView = new ModelAndView("user");
        modelAndView.addObject("user", user);
        modelAndView.addObject("cars", user.getCars());

        return modelAndView;
    }

    @GetMapping("/{id}/edit-profile")
    public ModelAndView getUserProfileEditPage(@PathVariable("id") UUID userId) {
        User user = userService.getById(userId);
        UserEditRequest userEditRequest = DtoMapper.mapUserToUserEditRequest(user);
        ModelAndView mav = new ModelAndView("edit-profile");
        mav.addObject("user", user);
        mav.addObject("userEditRequest", userEditRequest);

        return mav;
    }

    @PutMapping("/{id}/edit-profile")
    public String updateUserProfile(@PathVariable UUID id, UserEditRequest userEditRequest, HttpSession session) {
        userService.editUserDetails(id, userEditRequest);
        User updatedUser = userService.getById(id);
        session.setAttribute("user", updatedUser);

        return "redirect:/user";
    }
}



