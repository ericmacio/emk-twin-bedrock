package org.etyp.authorizer.client;

public class RequestResponse {

    private String body;
    private int statusCode;
    private long durationMs;
    private String error;

    public RequestResponse(String body, int statusCode, long durationMs, String error) {
        this.body = body;
        this.statusCode = statusCode;
        this.durationMs = durationMs;
        this.error = error;
    }

    public RequestResponse(String error) {
        this.error = error;
    }

    public String getBody() {
        return body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public long getDurationMs() {
        return durationMs;
    }

    public String getError() {
        return error == null ? "" : error;
    }
}
