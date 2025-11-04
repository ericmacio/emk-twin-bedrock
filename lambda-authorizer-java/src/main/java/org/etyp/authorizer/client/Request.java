package org.etyp.authorizer.client;

public record Request(
        String host,
        String path,
        String[] headers,
        String data
) {
}
