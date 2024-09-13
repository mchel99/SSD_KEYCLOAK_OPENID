package com.ssd.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.NullLdapAuthoritiesPopulator;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

//import javax.naming.directory.Attributes;
import java.util.List;

@Service
public class LDAPService {

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

    private LdapAuthenticationProvider createLdapAuthenticationProvider() {
        BindAuthenticator authenticator = new BindAuthenticator(contextSource);
        authenticator.setUserDnPatterns(new String[] { "cn={0},ou=users,ou=system" });
        return new LdapAuthenticationProvider(authenticator, new NullLdapAuthoritiesPopulator());
    }

    public boolean authenticate(String username, String password) {
        try {
            if (!userExists(username)) {
                throw new BadCredentialsException("Utente non trovato.");
            }

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
            Authentication authentication = ldapAuthenticationProvider.authenticate(authToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return authentication.isAuthenticated();
        } catch (AuthenticationException ex) {
            System.err.println("Errore durante l'autenticazione LDAP: " + ex.getMessage());
            return false;
        }
    }

    public boolean userExists(String username) {
        EqualsFilter filter = new EqualsFilter("cn", username);
        List<String> result = ldapTemplate.search(
                "ou=users,ou=system",
                filter.encode(),
                (AttributesMapper<String>) attributes -> (String) attributes.get("cn").get());
        return !result.isEmpty();
    }

    public boolean saveUser(String username, String sn, String password, String email, String employeeType) {
        try {
            // Controlla se l'utente esiste già
            if (userExists(username)) {
                // Se l'utente esiste già, ritorna false
                return false;
            }

            // Definisce il DN per il nuovo utente
            String dn = "cn=" + username + ",ou=users,ou=system";

            // Crea un nuovo contesto LDAP per il nuovo utente
            DirContextAdapter context = new DirContextAdapter(dn);

            // Imposta gli attributi dell'utente
            context.setAttributeValues("objectClass", new String[] { "top", "inetOrgPerson" });
            context.setAttributeValue("cn", username);
            context.setAttributeValue("sn", sn);
            context.setAttributeValue("mail", email);
            context.setAttributeValue("userPassword", password);
            context.setAttributeValue("employeeType", employeeType);

            // Salva il nuovo utente nel server LDAP
            ldapTemplate.bind(context);

            // Ritorna true se l'utente viene salvato correttamente
            return true;
        } catch (Exception e) {
            // Gestisce eventuali eccezioni durante il salvataggio
            e.printStackTrace();
            return false;
        }
    }

}
