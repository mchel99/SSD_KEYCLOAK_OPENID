package com.ssd.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.ssd.demo.service.LDAPService;

import jakarta.servlet.http.HttpSession;

import com.ssd.demo.model.User;
//import org.springframework.web.bind.annotation.RequestBody;

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
            @RequestParam("var2") String password,
            HttpSession session) {

        System.out.println("username : " + username);
        System.out.println("password : " + password);

        if (ldapService.authenticate(username, password)) {
            System.out.println("Dati ricevuti con successo");
            session.setAttribute("user", username);
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

    @PostMapping("/save-user")
    public ResponseEntity<String> saveUser(@RequestParam("user") String username,
            @RequestParam("pssw") String password,
            @RequestParam("mail") String email,
            @RequestParam("emplType") String employeeType) {

        // Chiamata al servizio LDAP per salvare l'utente
        boolean isUserCreated = ldapService.saveUser(username, username, password, email, employeeType);

        // Se l'utente viene creato con successo
        if (isUserCreated) {
            return ResponseEntity.ok("success");
        }
        // Se l'utente esiste già o si verifica un errore
        else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists or an error occurred");
        }
    }

    @GetMapping("/welcome")
    public String welcomePage(HttpSession session, Model model) {
        // Ottieni lo username dalla sessione
        String username = (String) session.getAttribute("user");
        // Se lo username è null, significa che non c'è nessun utente autenticato
        if (username == null) {
            return "redirect:/login"; // Redirect alla pagina di login se non autenticato
        }
        // Passa lo username al modello
        model.addAttribute("user", username);
        return "welcome"; // Nome del template Thymeleaf per la pagina di benvenuto
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // Invalidare la sessione per fare il logout
        session.invalidate();
        return "redirect:/login"; // Redirect alla pagina di login dopo il logout
    }
    
}