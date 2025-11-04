package org.etyp.authorizer.exceptions;

public class CheckApiKeyException extends RuntimeException {

    public CheckApiKeyException(String message) {
        super(message);
    }
}
