package com.ssd.demo.controller;

import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class GUIController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public RedirectView redirectToKeycloak() {
        // Effettua il reindirizzamento direttamente a
        // http://localhost:8080/oauth2/authorization/keycloak
        return new RedirectView("https://localhost:8443/oauth2/authorization/keycloak");
    }

    @GetMapping("/welcome")
    public String welcomePage() {
        return "welcome";
    }

    /*
     * @PostMapping("/login-variabiles")
     * public ResponseEntity<String> receiveLoginData(@RequestParam("var1") String
     * username,
     * 
     * @RequestParam("var2") String password,
     * HttpSession session) {
     * 
     * System.out.println("username : " + username);
     * System.out.println("password : " + password);
     * 
     * if (ldapService.authenticate(username, password)) {
     * System.out.println("Dati ricevuti con successo");
     * session.setAttribute("user", username);
     * return ResponseEntity.ok("success");
     * } else {
     * System.out.println("ERRORE");
     * return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
     * body("Si è verificato un errore interno");
     * }
     * }
     * 
     * @GetMapping("/signup")
     * public String signupPage() {
     * return "signup";
     * }
     * 
     * @PostMapping("/save-user")
     * public ResponseEntity<String> saveUser(@RequestParam("user") String username,
     * 
     * @RequestParam("pssw") String password,
     * 
     * @RequestParam("mail") String email,
     * 
     * @RequestParam("emplType") String employeeType) {
     * 
     * // Chiamata al servizio LDAP per salvare l'utente
     * boolean isUserCreated = ldapService.saveUser(username, username, password,
     * email, employeeType);
     * 
     * // Se l'utente viene creato con successo
     * if (isUserCreated) {
     * return ResponseEntity.ok("success");
     * }
     * // Se l'utente esiste già o si verifica un errore
     * else {
     * return ResponseEntity.status(HttpStatus.CONFLICT).
     * body("User already exists or an error occurred");
     * }
     * }
     * 
     * @GetMapping("/welcome")
     * public String welcomePage(HttpSession session, Model model) {
     * // Ottieni lo username dalla sessione
     * String username = (String) session.getAttribute("user");
     * // Se lo username è null, significa che non c'è nessun utente autenticato
     * if (username == null) {
     * return "redirect:/login"; // Redirect alla pagina di login se non autenticato
     * }
     * // Passa lo username al modello
     * model.addAttribute("user", username);
     * return "welcome"; // Nome del template Thymeleaf per la pagina di benvenuto
     * }
     * 
     * @GetMapping("/admin")
     * public String showAdmin(HttpSession session, Model model) {
     * XacmlService xacmlService = new XacmlService();
     * int result = -1;
     * String username = (String) session.getAttribute("user");
     * String role = ldapService.getUserRole(username);
     * try {
     * result = xacmlService.doFilter(role, "/admin", "read");
     * System.out.
     * println("result meaning: 0=permit, 1=deny, 2=indeterminate, 3=not applicable"
     * );
     * System.out.println("result: " + result);
     * } catch (IOException e) {
     * e.printStackTrace();
     * }
     * // Carica la pagina solo se l'utente ha il permesso di accedere (gestione in
     * // XACML)
     * if (result == 0) {
     * return "admin";
     * } else {
     * return "accessDenied"; // o reindirizzamento a una pagina di accesso negato
     * }
     * }
     * 
     * @GetMapping("/user")
     * public String showUser(HttpSession session, Model model) {
     * XacmlService xacmlService = new XacmlService();
     * int result = -1;
     * String username = (String) session.getAttribute("user");
     * String role = ldapService.getUserRole(username);
     * try {
     * result = xacmlService.doFilter(role, "/user", "read");
     * System.out.
     * println("result meaning: 0=permit, 1=deny, 2=indeterminate, 3=not applicable"
     * );
     * System.out.println("result: " + result);
     * } catch (IOException e) {
     * e.printStackTrace();
     * }
     * // Carica la pagina solo se l'utente ha il permesso di accedere (gestione in
     * // XACML)
     * if (result == 0) {
     * return "user";
     * } else {
     * return "accessDenied"; // o reindirizzamento a una pagina di accesso negato
     * }
     * }
     * 
     * @GetMapping("/logout")
     * public String logout(HttpSession session) {
     * // Invalidare la sessione per fare il logout
     * session.invalidate();
     * return "redirect:/login"; // Redirect alla pagina di login dopo il logout
     * }
     * }
     */

}