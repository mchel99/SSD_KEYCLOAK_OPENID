package com.ssd.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/js/**", "/images/**", "/bootstrap-4/**", "/jquery/**").permitAll() // Permetti l'accesso ai file statici
                .requestMatchers("/login").permitAll()     // Consenti l'accesso alla pagina di login
                .requestMatchers("/signup").permitAll()    // Consenti l'accesso alla pagina di registrazione
                .anyRequest().authenticated()              // Richiedi autenticazione per tutte le altre pagine
            )
            .formLogin(form -> form
                .loginPage("/login")                       // Specifica la pagina di login personalizzata
                .permitAll()                               // Consenti l'accesso a tutti alla pagina di login
            )
            .logout(logout -> logout
                .permitAll());                             // Consenti a tutti di accedere al logout

        return http.build();
    }

    // Configura utenti in memoria per test
    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
            User.withUsername("user")
                .password("{noop}password")   // Usa {noop} per indicare che la password non è codificata
                .roles("USER")
                .build()
        );
    }
}

