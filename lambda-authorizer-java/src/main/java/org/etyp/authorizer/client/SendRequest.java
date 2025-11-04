package org.etyp.authorizer.client;

import org.etyp.authorizer.LambdaAuthorizerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.Instant;

public class SendRequest {

    private final static Logger LOGGER = LoggerFactory.getLogger(SendRequest.class);

    public static RequestResponse get(Request request) {

        HttpResponse<String> response = null;
        String error = null;
        HttpClient client = HttpClient.newBuilder().build();

        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(new URI(request.host() + request.path()))
                    .GET()
                    .headers(request.headers())
                    .build();
            Instant start = Instant.now();
            response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString(Charset.defaultCharset()));
            Instant finish = Instant.now();
            long durationMs = Duration.between(start, finish).toMillis();
            if(response.statusCode() != 200) {
                LOGGER.info("ERROR. Bad status code received: {}", response.statusCode());
                error = "Bad status code";
            }
            return new RequestResponse(response.body(), response.statusCode(), durationMs, error);

        } catch (IOException | InterruptedException | URISyntaxException e) {
            LOGGER.error("Exception received: {}", e.getMessage());
            return new RequestResponse("Request got an Exception");
        }
    }

    public static RequestResponse post(Request request) {

        HttpResponse<String> response = null;
        String error = null;

        try (HttpClient client = HttpClient.newBuilder().build()) {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(new URI(request.host() + request.path()))
                    .POST(HttpRequest.BodyPublishers.ofString(request.data()))
                    .headers(request.headers())
                    .build();
            Instant start = Instant.now();
            response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString(Charset.defaultCharset()));
            Instant finish = Instant.now();
            long durationMs = Duration.between(start, finish).toMillis();
            if(response.statusCode() != 200) {
                LOGGER.error("ERROR. Bad status code received: {}", response.statusCode());
                error = "Bad status code";
            }
            return new RequestResponse(response.body(), response.statusCode(), durationMs, error);

        } catch (IOException | InterruptedException | URISyntaxException e) {
            LOGGER.error("Exception received: {}", e.getMessage());
            return new RequestResponse("Request got an Exception");
        }
    }

}
