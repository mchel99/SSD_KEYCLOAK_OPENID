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

@Service
public class XacmlService {

    @Autowired
    private PDP pdp;

    public boolean isAccessAllowed(String role) {
        try {
            // Creiamo una richiesta XACML simulata per il ruolo dell'utente
            RequestCtx request = buildXacmlRequest(role);

            // Valutiamo la richiesta XACML
            ResponseCtx response = pdp.evaluate(request);

            // Interpretiamo la risposta e decidiamo se consentire o negare l'accesso
            return interpretResponse(response);
        } catch (ParsingException e) {
            e.printStackTrace();
            return false;
        }
    }

    private RequestCtx buildXacmlRequest(String role) throws ParsingException {
        // Costruiamo la richiesta XACML basata sul ruolo (subject-role)
        String requestXml = String.format(
            "<Request xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\">" +
            "<Attributes Category=\"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject\">" +
            "<Attribute AttributeId=\"subject-role\">" +
            "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">%s</AttributeValue>" +
            "</Attribute>" +
            "</Attributes>" +
            "</Request>", role);

        InputStream requestStream = new ByteArrayInputStream(requestXml.getBytes(StandardCharsets.UTF_8));
        return RequestCtx.getInstance(requestStream);
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
