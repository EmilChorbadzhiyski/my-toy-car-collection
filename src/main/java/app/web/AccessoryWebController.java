package app.web;

import app.accessory.service.AccessoryClientService;
import app.security.AuthenticationMetadata;
import app.web.dto.AccessoryCreateRequest;
import app.web.dto.AccessoryResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/accessories")
public class AccessoryWebController {

    private final AccessoryClientService accessoryClientService;

    public AccessoryWebController(AccessoryClientService accessoryClientService) {
        this.accessoryClientService = accessoryClientService;
    }

    @GetMapping
    public ModelAndView getAccessoriesPage() {
        ModelAndView modelAndView = new ModelAndView("accessories");
        List<AccessoryResponse> accessories = accessoryClientService.getAllAccessories();
        modelAndView.addObject("accessories", accessories);
        return modelAndView;
    }

    @GetMapping("/add")
    public ModelAndView showAddAccessoryForm() {
        return new ModelAndView("add-accessory")
                .addObject("accessoryRequest", new AccessoryCreateRequest());
    }

    @PostMapping("/add")
    public String handleAddAccessory(@ModelAttribute("accessoryRequest") AccessoryCreateRequest accessoryRequest,
                                     @AuthenticationPrincipal AuthenticationMetadata user) {
        if (user == null){
            System.out.println("user is null");
            return "redirect:/login";
        }
        UUID userId = user.getUserId();
        accessoryRequest.setUserId(userId);
        accessoryClientService.createAccessory(accessoryRequest);

        return "redirect:/accessories";
    }

    @GetMapping("/delete/{id}")
    public String handleDeleteAccessory(@PathVariable UUID id) {
        accessoryClientService.deleteAccessory(id);
        return "redirect:/accessories";
    }
}
