package com.ssd.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.ssd.demo.model.*;
import com.ssd.demo.Service.LDAPService;

@Controller
public class GUIController {

    User user = new User();
    private final LDAPService ldapService;

    @Autowired
    public GUIController(LDAPService ldapService) {
        this.ldapService = ldapService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Nome del template Thymeleaf per la pagina1.html
    }

    @PostMapping("/login-variabiles")
    public ResponseEntity<String> receiveLoginData(@RequestParam("var1") String username,
            @RequestParam("var2") String password) {

        System.out.println("username : " + username);
        System.out.println("password : " + password);

        user.setUsername(username);
        user.setPassword(password);

        // Salva i valori in una variabile o esegui altre operazioni necessarie
        if (ldapService.authenticate(user.getUsername(), user.getPassword())) {
            System.out.println("sto dando una okokoko");
            return ResponseEntity.ok("Dati ricevuti con successo");
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Si è verificato un errore interno");

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
