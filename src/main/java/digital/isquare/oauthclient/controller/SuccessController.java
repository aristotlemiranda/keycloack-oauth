package digital.isquare.oauthclient.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login/oauth2/code")
public class SuccessController {
    @GetMapping("/springboot-openid-client-app")
    public String showContactPage() {
        System.out.println("EUREKA!!!");
      return  "EUREKA";
    }

}
