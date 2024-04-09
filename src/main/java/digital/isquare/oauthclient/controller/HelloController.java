package digital.isquare.oauthclient.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/api/public")
    public ResponseEntity<String> getPublic() {
        return ResponseEntity.ok("Contenu Public");
    }
    @GetMapping("/api/private")
    public ResponseEntity<String> getPrivate() {
        return ResponseEntity.ok("Contenu Privé");
    }
}
