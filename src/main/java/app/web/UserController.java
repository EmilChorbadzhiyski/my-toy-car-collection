package app.web;

import app.security.AuthenticationMetadata;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.UserEditRequest;
import app.web.mapper.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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
    public ModelAndView getUserProfilePage(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        User user = userService.getById(authenticationMetadata.getUserId());

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
    public String updateUserProfile(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata,
                                    @PathVariable UUID id,
                                    @ModelAttribute UserEditRequest userEditRequest) {
        UUID authenticatedUserId = authenticationMetadata.getUserId();

        if (!authenticatedUserId.equals(id)) {
            return "redirect:/user";
        }
        userService.editUserDetails(id, userEditRequest);

        return "redirect:/user";
    }
}



