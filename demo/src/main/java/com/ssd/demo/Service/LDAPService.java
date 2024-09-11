package com.ssd.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.naming.directory.Attributes;
import java.util.List;

@Service
public class LDAPService {

    public LDAPService() {
        this.ldapAuthenticationProvider = null;
    }

    @Autowired
    private LdapTemplate ldapTemplate;

    @Autowired
    private LdapContextSource contextSource;

    private final LdapAuthenticationProvider ldapAuthenticationProvider;

    public LDAPService(LdapTemplate ldapTemplate, LdapContextSource contextSource) {
        this.ldapTemplate = ldapTemplate;
        this.contextSource = contextSource;
        this.ldapAuthenticationProvider = createLdapAuthenticationProvider();
    }

    /**
     * Crea un LdapAuthenticationProvider utilizzando BindAuthenticator.
     */
    private LdapAuthenticationProvider createLdapAuthenticationProvider() {
        // Configura l'autenticatore di bind
        BindAuthenticator authenticator = new BindAuthenticator(contextSource);
        authenticator.setUserDnPatterns(new String[] { "uid={0},ou=people" });

        // Crea l'auth provider
        return new LdapAuthenticationProvider(authenticator);
    }

    /**
     * Autentica l'utente contro il server LDAP.
     *
     * @param username il nome utente fornito
     * @param password la password fornita
     * @return true se l'autenticazione è avvenuta con successo, false altrimenti
     * @throws AuthenticationException se si verifica un errore durante
     *                                 l'autenticazione
     */
    public boolean authenticate(String username, String password) {
        try {
            // Verifica se l'utente esiste in LDAP
            if (!userExists(username)) {
                throw new BadCredentialsException("Utente non trovato.");
            }

            // Autentica l'utente utilizzando UsernamePasswordAuthenticationToken
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
            Authentication authentication = ldapAuthenticationProvider.authenticate(authToken);

            // Salva l'autenticazione nel contesto di sicurezza di Spring
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return authentication.isAuthenticated();
        } catch (AuthenticationException ex) {
            // Gestisci eventuali errori di autenticazione
            System.err.println("Errore durante l'autenticazione LDAP: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Controlla se un utente esiste in LDAP.
     *
     * @param username il nome utente da cercare
     * @return true se l'utente esiste, false altrimenti
     */
    public boolean userExists(String username) {
        // Filtra per l'attributo uid dell'utente
        EqualsFilter filter = new EqualsFilter("uid", username);

        // Cerca l'utente nell'OU "people" per vedere se esiste
        List<String> result = ldapTemplate.search(
                "ou=people", // Base di ricerca
                filter.encode(), // Filtro LDAP
                (AttributesMapper<String>) attributes -> {
                    // Ritorna l'UID se presente
                    return (String) attributes.get("uid").get();
                });

        // Verifica se il risultato contiene almeno un elemento
        return !result.isEmpty();
    }
}
