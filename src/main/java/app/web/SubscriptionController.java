package app.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SubscriptionController {


    @GetMapping("/subscription")
    public String getSubscriptionPage() {
        return "subscription";
    }
}
