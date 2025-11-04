package org.etyp.authorizer;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import org.etyp.authorizer.aws.AwsUtils;
import org.etyp.authorizer.io.AuthPolicy;
import org.etyp.authorizer.auth.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.regions.Region;

import java.util.HashMap;
import java.util.Map;

public class LambdaAuthorizerRequest implements RequestHandler<APIGatewayProxyRequestEvent, AuthPolicy> {

    private final static Logger LOGGER = LoggerFactory.getLogger(LambdaAuthorizerRequest.class);
    private final static String REGION = System.getenv("REGION");
    private final static String HEADER_API_KEY = System.getenv("HEADER_API_KEY");
    private final static String SSM_API_KEY_NAME = System.getenv("SSM_API_KEY_NAME"); // /dev/emk-twin-bedrock/API_KEY
    private static String apiKey = "NULL";

    private static boolean apiKeyOk() {
        return (apiKey!=null && !apiKey.equals("NULL"));
    }

    static {
        getApiKeyFromSSM();
        LOGGER.info("Static data initialization: {}", (apiKeyOk() ? "OK" : "FAILED"));
    }

    @Override
    public AuthPolicy handleRequest(APIGatewayProxyRequestEvent request, Context context) {

        APIGatewayProxyRequestEvent.ProxyRequestContext proxyContext = request.getRequestContext();

        if (initDone()) {
            Auth auth = new Auth(proxyContext, REGION);
            Map<String, String> requestHeaders = request.getHeaders();
            Map<String, String> headers = new HashMap<>();
            for(String key : requestHeaders.keySet()) {
                LOGGER.debug("{}: {}", key, requestHeaders.get(key));
                headers.put(key.toLowerCase(), requestHeaders.get(key));
            }
            if (headers.containsKey(HEADER_API_KEY.toLowerCase())) {
                return auth.getApiKeyAuthPolicy(apiKey, headers.get(HEADER_API_KEY));
            } else {
                LOGGER.error("ERROR: No apiKey available");
                return auth.getAuthPolicy(null, false);
            }
        } else {
            LOGGER.error("ERROR: Failed to initialize");
            return new Auth(proxyContext, REGION).getAuthPolicy(null, false);
        }

    }

    private static void getApiKeyFromSSM() {
        if (!apiKeyOk()) {
            LOGGER.info("Get API KEY from SSM ...");
            try {
                apiKey = AwsUtils.getParameterValue(Region.of(REGION), SSM_API_KEY_NAME);
                LOGGER.info("apiKey: {}",apiKey);
            } catch (Exception e) {
                LOGGER.error("ERROR: Exception when getting SSMParametersValue: {}", e.getMessage());
            }
        }
    }

    private static boolean initDone() {

        int nbRetry = 0;
        int maxRetry = 3;

        while (!apiKeyOk() && nbRetry++ < maxRetry) {
            LOGGER.info("apiKey is NULL. Get apiKey from SSM");
            getApiKeyFromSSM();
        }
        if (nbRetry == 0) {
            LOGGER.info("Initialization OK");
        } else {
            LOGGER.info("nbRetry: {}", nbRetry);
        }
        return apiKeyOk();
    }

}