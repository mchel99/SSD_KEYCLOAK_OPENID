package com.ssd.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.LdapAuthenticator;
import org.springframework.security.ldap.authentication.NullLdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.LdapUserDetailsManager;

@Configuration
public class LdapConfig {

    /**
     * Configura la sorgente del contesto LDAP (LdapContextSource).
     * 
     * @return LdapContextSource configurato.
     */
    @Bean
    public LdapContextSource ldapContextSource() {
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl("ldap://localhost:10389");  // URL del server LDAP
        //contextSource.setBase("ou=users,ou=system");     // Base DN (distinguished name)
        contextSource.setUserDn("uid=admin,ou=system");  // DN dell'utente admin
        contextSource.setPassword("secret");             // Password dell'utente admin
        contextSource.setPooled(true);                   // Abilita il pooling delle connessioni
        return contextSource;
    }

    /**
     * Configura il bean LdapTemplate per facilitare le operazioni di ricerca e manipolazione dei dati LDAP.
     * 
     * @param contextSource LdapContextSource configurato.
     * @return LdapTemplate configurato.
     */
    @Bean
    public LdapTemplate ldapTemplate(LdapContextSource contextSource) {
        return new LdapTemplate(contextSource);
    }

    /**
     * Configura l'oggetto LdapAuthenticator utilizzando BindAuthenticator.
     * Questo autenticatore è responsabile di eseguire il binding con LDAP usando il DN dell'utente.
     * 
     * @param contextSource LdapContextSource configurato.
     * @return LdapAuthenticator configurato.
     */
    @Bean
    public LdapAuthenticator ldapAuthenticator(LdapContextSource contextSource) {
        BindAuthenticator bindAuthenticator = new BindAuthenticator(contextSource);
        bindAuthenticator.setUserDnPatterns(new String[] {"cn={0},ou=users,ou=system"}); // Modifica se necessario
        return bindAuthenticator;
    }

    /**
     * Configura LdapAuthenticationProvider, che viene utilizzato da Spring Security per autenticare gli utenti LDAP.
     * 
     * @param ldapAuthenticator LdapAuthenticator configurato.
     * @return LdapAuthenticationProvider configurato.
     */
    @Bean
    public LdapAuthenticationProvider ldapAuthenticationProvider(LdapAuthenticator ldapAuthenticator) {
        return new LdapAuthenticationProvider(ldapAuthenticator, new NullLdapAuthoritiesPopulator());
    }

    /**
     * Configura l'oggetto LdapUserDetailsManager per la gestione degli utenti e delle operazioni CRUD sugli utenti LDAP.
     * 
     * @param contextSource LdapContextSource configurato.
     * @return LdapUserDetailsManager configurato.
     */
    @Bean
    public LdapUserDetailsManager ldapUserDetailsManager(LdapContextSource contextSource) {
        return new LdapUserDetailsManager(contextSource);
    }
}
