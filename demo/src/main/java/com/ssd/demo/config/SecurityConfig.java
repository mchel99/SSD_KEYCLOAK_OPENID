package com.ssd.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;
import org.springframework.security.ldap.userdetails.LdapUserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final LdapAuthenticationProvider ldapAuthenticationProvider;
    private final LdapContextSource ldapContextSource;

    public SecurityConfig(LdapAuthenticationProvider ldapAuthenticationProvider, LdapContextSource ldapContextSource) {
        this.ldapAuthenticationProvider = ldapAuthenticationProvider;
        this.ldapContextSource = ldapContextSource;
    }

    @Bean
    public FilterBasedLdapUserSearch ldapUserSearch() {
        return new FilterBasedLdapUserSearch("ou=users,ou=system", "cn={0}", ldapContextSource);
    }

    @Bean
    public LdapUserDetailsMapper ldapUserDetailsMapper() {
        return new LdapUserDetailsMapper();
    }

    @Bean
    public LdapUserDetailsService ldapUserDetailsService() {
        return new LdapUserDetailsService(ldapUserSearch()); // Usa la ricerca e il mapper configurati
    }

    /**
     * Configura le regole di sicurezza per le richieste HTTP.
     * 
     * @param http l'oggetto HttpSecurity per configurare le regole di sicurezza.
     * @return SecurityFilterChain configurato.
     * @throws Exception se si verifica un errore durante la configurazione.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disabilita la protezione CSRF per tutte le richieste
                .authorizeHttpRequests(auth -> auth
                        // Permetti l'accesso ai file statici senza autenticazione
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/bootstrap-4/**", "/jquery/**").permitAll()
                        // Permetti l'accesso alla pagina di login senza autenticazione
                        .requestMatchers("/login").permitAll()
                        // Permetti l'accesso alla pagina di registrazione senza autenticazione
                        .requestMatchers("/signup").permitAll()
                        // Permetti l'accesso alla pagina di welcome senza autenticazione
                        .requestMatchers("/welcome").permitAll()
                        // Permetti l'accesso a POST /login-variabiles senza autenticazione
                        .requestMatchers("/login-variabiles").permitAll()
                        // Permetti l'accesso a POST /save-user senza autenticazione
                        .requestMatchers("/save-user").permitAll()
                        // Permetti di effettuare la session invalidation per il logout
                        .requestMatchers("/logout").permitAll()
                        // Permetti l'accesso ai file HTML specificati senza autenticazione
                        .requestMatchers("/resource1.html", "/resource2.html", "/resource3.html").permitAll()
                        // Permetti l'accesso ai percorsi pubblici senza autenticazione
                        .requestMatchers("/public/**").permitAll()
                        // Richiedi autenticazione per tutte le altre pagine
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login") // Specifica la pagina di login personalizzata
                        .permitAll() // Consenti l'accesso a tutti alla pagina di login
                )
                .logout(logout -> logout
                        .permitAll() // Consenti a tutti di accedere al logout
                )

                .authenticationProvider(ldapAuthenticationProvider);

        return http.build(); // Costruisce e restituisce la configurazione di sicurezza
    }
}
