package org.etyp.authorizer.auth;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import org.etyp.authorizer.exceptions.CheckApiKeyException;
import org.etyp.authorizer.helper.Utils;
import org.etyp.authorizer.io.ApiKeyClaim;
import org.etyp.authorizer.io.AuthPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


public class Auth {

    private final static Logger LOGGER = LoggerFactory.getLogger(Auth.class);

    private final APIGatewayProxyRequestEvent.ProxyRequestContext proxyContext;
    private final String region;

    public Auth(APIGatewayProxyRequestEvent.ProxyRequestContext proxyContext, String region) {
        this.proxyContext = proxyContext;
        this.region = region;
    }

    public AuthPolicy getApiKeyAuthPolicy(String apiKey, String requestApiKey) {
        LOGGER.info("requestApiKey: {}", requestApiKey);
        try {
            checkApiKey(apiKey, requestApiKey);
        } catch(CheckApiKeyException e) {
            LOGGER.error("ERROR: Exception from checkApiKey: {}", e.getMessage());
            return getAuthPolicy(null, false);
        }
        return getAuthPolicy(getApiKeyPrincipalId(apiKey), true);
    }

    public AuthPolicy getAuthPolicy(String principalId, boolean allow) {
        LOGGER.info(allow ? "Allow access" : "Access denied");
        return allow ?
                new AuthPolicy(principalId,
                        AuthPolicy.PolicyDocument.getAllowOnePolicy(region, proxyContext.getAccountId(), proxyContext.getApiId(),
                                proxyContext.getStage(), AuthPolicy.HttpMethod.valueOf(proxyContext.getHttpMethod()), proxyContext.getResourcePath()))
                :
                new AuthPolicy(principalId,
                        AuthPolicy.PolicyDocument.getDenyAllPolicy(region, proxyContext.getAccountId(), proxyContext.getApiId(),
                                proxyContext.getStage()));
    }

    public static void displayPolicy(AuthPolicy authPolicy) {
        LOGGER.info("---> Policy:");
        LOGGER.info("PrincipalId: {}", authPolicy.getPrincipalId());
        Map<String, Object> policyDocument = authPolicy.getPolicyDocument();
        LOGGER.info("Policy Document:");
        for (String key : policyDocument.keySet()) {
            if (key.equals("Statement")) {
                LOGGER.info("Statements:");
                Map<String, Object>[] statements = (Map<String, Object>[]) policyDocument.get(key);
                int statementId = 0;
                for (Map<String, Object> statement : statements) {
                    LOGGER.info("[{}]", statementId++);
                    for (String statementKey : statement.keySet()) {
                        LOGGER.info("{}: {}", statementKey, statement.get(statementKey));
                    }
                }
            } else {
                LOGGER.info("{}: {}", key, policyDocument.get(key));
            }
        }
    }

    private void checkApiKey(String apiKey, String requestApiKey) {
        if(apiKey.equals(requestApiKey)) {
            String decodedString = Utils.decodeApiKey(apiKey)
                    .orElseThrow(() -> new CheckApiKeyException("Cannot decode API key"));
            ApiKeyClaim claim = getClaimFromDecodedApiKey(decodedString);
            String userId = claim.getUserId();
            String email = claim.getEmail();
            LOGGER.info("userId: {}",userId);
            LOGGER.info("email: {}",email);
            if (email == null) {
                throw new CheckApiKeyException("Cannot get email from API key");
            }
            LOGGER.info("Api key is valid");
        } else {
            throw new CheckApiKeyException("Bad API key");
        }
    }

    private  static ApiKeyClaim getClaimFromDecodedApiKey(String decodedApiKey) {
        final String expirePattern = "expiry_times";
        final String userIdPattern = "user_id";
        final String emailPattern = "email";
        ApiKeyClaim claim = new ApiKeyClaim();
        String[] fields = decodedApiKey.split(",");
        for(String field: fields) {
            if(field.contains(expirePattern)) {
                claim.setExpire(field.split(":")[1].strip().replace("\"", "").replace("}", ""));
            }
            if(field.contains(userIdPattern)) {
                claim.setUserId(field.split(":")[1].strip().replace("\"", "").replace("}", ""));
            }
            if(field.contains(emailPattern)) {
                claim.setEmail(field.split(":")[1].strip().replace("\"", "").replace("}", ""));
            }
        }
        return claim;
    }

    private String getApiKeyPrincipalId(String apiKey) {
        return "default_principal_id";
    }

}
