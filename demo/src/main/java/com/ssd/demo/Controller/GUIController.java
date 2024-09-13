package com.ssd.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.ssd.demo.Service.LDAPService;
import com.ssd.demo.model.User;

@Controller
public class GUIController {

    User user = new User();
    @Autowired
    private LDAPService ldapService;

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Nome del template Thymeleaf per la pagina di login
    }

    @PostMapping("/login-variabiles")
    public ResponseEntity<String> receiveLoginData(@RequestParam("var1") String username,
            @RequestParam("var2") String password) {

        System.out.println("username : " + username);
        System.out.println("password : " + password);

        if (ldapService.authenticate(username, password)) {
            System.out.println("Dati ricevuti con successo");
            return ResponseEntity.ok("success");
        } else {
            System.out.println("ERRORE");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Si è verificato un errore interno");
        }
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @GetMapping("/welcome")
    public String welcomePage() {
        return "welcome";
    }
}
