package com.ssd.demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
        return new RedirectView("https://localhost:8081/oauth2/authorization/keycloak");
    }

    @GetMapping("/public-page")
    public String publicPage() {
        return "public-page";
    }

    @GetMapping("/admin-page")
    public String adminPage(Model model, @AuthenticationPrincipal OidcUser user) {
        model.addAttribute("email", user.getEmail());
        return "admin-page";
    }

    @GetMapping("/user-page")
    public String userPage(Model model, @AuthenticationPrincipal OidcUser user) {
        model.addAttribute("email", user.getEmail());
        return "user-page";
    }

    @GetMapping("/admin-user-page")
    public String adminAndUserPage(Model model, @AuthenticationPrincipal OidcUser user) {
        model.addAttribute("email", user.getEmail());
        return "admin-user-page";
    }

}