package com.ssd.demo.config;

import com.sun.xacml.PDP;
import com.sun.xacml.PDPConfig;
import com.sun.xacml.finder.impl.FilePolicyModule;
import com.sun.xacml.finder.PolicyFinder;
import com.sun.xacml.finder.PolicyFinderModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class XacmlPdpConfig {

    @Bean
    public PDP pdp() {
        try {
            // Carica il file di policy XACML dalla directory resources/xacml
            URI policyFileUri = getClass().getClassLoader().getResource("xacml/policy.xml").toURI();
            File policyFile = new File(policyFileUri);

            // Configura il PolicyFinder per trovare le policy
            PolicyFinder policyFinder = new PolicyFinder();
            Set<PolicyFinderModule> policyModules = new HashSet<>();

            // Aggiunge il modulo FilePolicyModule con il file di policy
            FilePolicyModule filePolicyModule = new FilePolicyModule();
            filePolicyModule.addPolicy(policyFile.getPath());
            policyModules.add(filePolicyModule);

            // Configura il PolicyFinder
            policyFinder.setModules(policyModules);

            // Crea il PDPConfig e ritorna il PDP
            PDPConfig pdpConfig = new PDPConfig(null, policyFinder, null);
            return new PDP(pdpConfig);

        } catch (Exception e) {
            throw new RuntimeException("Errore nella configurazione del PDP", e);
        }
    }
}
