package com.ssd.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class GUIController {
    

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Nome del template Thymeleaf per la pagina1.html
    }

/*
    @PostMapping("/login-variabiles")
    public ResponseEntity<String> receiveLoginData(@RequestParam("var1") String username,
            @RequestParam("var2") String password) {
 
        System.out.println("username : " + username);
        System.out.println("password : " + password);
 
        p1.setUsername(username);
        p1.setPassword(password);
 
        // Salva i valori in una variabile o esegui altre operazioni necessarie
        if (com.g2.Interfaces.t2_3.verifyLogin(username, password)) {
            return ResponseEntity.ok("Dati ricevuti con successo");
        }
 
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Si è verificato un errore interno");
 
    }
*/
    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @GetMapping("/welcome")
    public String welcomePage() {
        return "welcome";
    }
    
        
}
