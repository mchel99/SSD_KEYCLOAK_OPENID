package com.ssd.demo.service;

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.springframework.stereotype.Service;

import com.sun.xacml.PDP;
import com.sun.xacml.PDPConfig;
import com.sun.xacml.ctx.RequestCtx;
import com.sun.xacml.ctx.ResponseCtx;
import com.sun.xacml.ctx.Result;
import com.sun.xacml.finder.AttributeFinder;
import com.sun.xacml.finder.PolicyFinder;
import com.sun.xacml.finder.impl.CurrentEnvModule;
import com.sun.xacml.finder.impl.FilePolicyModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;


@Service
public class XacmlService {

    private static final Logger logger = LoggerFactory.getLogger(XacmlService.class);

    // Contiene le policy disponibili
    //private File[] listaFile;

    // Costruttore vuoto
    public XacmlService() {
    }

    public Integer doFilter(String role, String resource, String action) throws IOException {
        FilePolicyModule policyModule = new FilePolicyModule();
        PolicyFinder policyFinder = new PolicyFinder();
        Set<FilePolicyModule> policyModules = new HashSet<>();

        // Usa ClassPathResource per ottenere il percorso della directory
        Resource resourceDir = new ClassPathResource("policy/");
        File policyDirectory = resourceDir.getFile();

        logger.info("PATH_POLICY: " + policyDirectory.getAbsolutePath());

        if (!policyDirectory.exists() || !policyDirectory.isDirectory()) {
            logger.error("Directory delle policy non trovata o non è una directory.");
            return -1;
        }

        File[] listaFile = policyDirectory.listFiles();
        if (listaFile == null || listaFile.length == 0) {
            logger.error("Nessun file di policy trovato nella directory.");
            return -1;
        }

        // Carica tutte le policy
        for (File file : listaFile) {
            String policyFile = file.getAbsolutePath();
            try {
                policyModule.addPolicy(policyFile);
                policyModules.add(policyModule);
            } catch (Exception e) {
                logger.error("Errore durante il caricamento della policy: " + policyFile, e);
            }
        }

        policyFinder.setModules(policyModules);

        CurrentEnvModule envModule = new CurrentEnvModule();
        AttributeFinder attrFinder = new AttributeFinder();
        List<CurrentEnvModule> attrModules = new ArrayList<>();
        attrModules.add(envModule);
        attrFinder.setModules(attrModules);

        try {
            RequestCtx xacmlRequest = RequestBuilder.createXACMLRequest(role, resource, action);

            PDP pdp = new PDP(new PDPConfig(attrFinder, policyFinder, null));
            ResponseCtx xacmlResponse = pdp.evaluate(xacmlRequest);

            // Gestisci tutti i risultati
            Set<Result> results = xacmlResponse.getResults();
            for (Result result : results) {
                int decision = result.getDecision();
                if (decision == Result.DECISION_PERMIT) {
                    return 0; // Permesso
                } else if (decision == Result.DECISION_DENY) {
                    return 1; // Negato
                } else if (decision == Result.DECISION_NOT_APPLICABLE || decision == Result.DECISION_INDETERMINATE) {
                    return 2; // Non applicabile o indeterminato
                }
            }
        } catch (Exception ex) {
            logger.error("Errore durante la valutazione XACML", ex);
        }

        return -1; // Errore generico
    }

}
