package com.ssd.demo.service;

import com.sun.xacml.PDP;
import com.sun.xacml.ParsingException;
import com.sun.xacml.ctx.RequestCtx;
import com.sun.xacml.ctx.ResponseCtx;
import com.sun.xacml.ctx.Result;
import com.sun.xacml.ctx.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class XacmlService {

    private static final Logger LOGGER = Logger.getLogger(XacmlService.class.getName());

    @Autowired
    private PDP pdp;

    public boolean isAccessAllowed(String role, String resource) {
        try {
            // Creiamo una richiesta XACML simulata per il ruolo dell'utente e la risorsa
            String webres = "http://localhost:8080/" + resource;
            RequestCtx request = buildXacmlRequest(role, webres);

            // Valutiamo la richiesta XACML
            ResponseCtx response = pdp.evaluate(request);

            // Interpretiamo la risposta e decidiamo se consentire o negare l'accesso
            return interpretResponse(response);
        } catch (ParsingException e) {
            LOGGER.log(Level.SEVERE, "Errore nella costruzione della richiesta XACML", e);
            return false;
        }
    }

    private RequestCtx buildXacmlRequest(String role, String resource) throws ParsingException {
        // Costruzione della richiesta XACML basata sul ruolo e sulla risorsa
        String requestXml = String.format(
            "<Request xmlns=\"urn:oasis:names:tc:xacml:1.0:core:schema\">" +
            "<Attributes Category=\"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject\">" +
            "<Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject:subject-id\">" +
            "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">%s</AttributeValue>" +
            "</Attribute>" +
            "</Attributes>" +
            "<Attributes Category=\"urn:oasis:names:tc:xacml:1.0:resource-category\">" +
            "<Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:resource:resource-id\">" +
            "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">%s</AttributeValue>" +
            "</Attribute>" +
            "</Attributes>" +
            "<Attributes Category=\"urn:oasis:names:tc:xacml:1.0:action-category\">" +
            "<Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:action:action-id\">" +
            "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">read</AttributeValue>" +
            "</Attribute>" +
            "</Attributes>" +
            "</Request>", role, resource
        );
    
        // Stampa per il debug
        System.out.println("Request XML: " + requestXml);
    
        try (InputStream requestStream = new ByteArrayInputStream(requestXml.getBytes(StandardCharsets.UTF_8))) {
            RequestCtx requestCtx = RequestCtx.getInstance(requestStream);
            if (requestCtx == null) {
                throw new ParsingException("Failed to create RequestCtx from XML.");
            }
            return requestCtx;
        } catch (Exception e) {
            // Gestione delle eccezioni per una diagnosi migliore
            throw new ParsingException("Error processing XACML request: " + e.getMessage(), e);
        }
    }

    private boolean interpretResponse(ResponseCtx response) {
        // Logica per interpretare la risposta XACML
        // Verifica se la decisione è "Permit" o "Deny"
        Set<Result> results = response.getResults();
        if (results.isEmpty()) {
            return false; // Nessun risultato disponibile
        }

        // Controlliamo solo il primo risultato
        Result result = results.iterator().next();
        Status status = result.getStatus();
        int decision = result.getDecision();

        // Controlla se il risultato è una decisione di "Permit"
        return decision == Result.DECISION_PERMIT;
    }
}
