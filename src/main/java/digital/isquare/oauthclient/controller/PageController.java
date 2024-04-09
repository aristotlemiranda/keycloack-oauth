package digital.isquare.oauthclient.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/service")
    public String showServicesPage() {
        return "service";
    }

    @GetMapping("/contact")
    public String showContactPage() {
        return "contact";
    }

    @GetMapping("/about")
    public String showAboutPage() {
        return "about";
    }

    @GetMapping("/privacy")
    public String showPrivacyPage() {
        return "privacy";
    }
}
