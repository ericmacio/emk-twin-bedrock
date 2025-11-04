package org.etyp.authorizer.helper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Optional;

public class Utils {

    public static Optional<String> decodeApiKey(String apiKey) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(apiKey);
            return Optional.of(new String(decodedBytes));
        } catch(IllegalArgumentException e) {
            return Optional.empty();
        }

    }

    public static String getLocalDateTimeStr(Long timestamp) {
        return Instant.ofEpochMilli(timestamp)
                .atZone(ZoneId.of("Europe/Paris"))
                .toLocalDateTime().toString();
    }

    public static LocalDateTime convertLocalDateTime(Instant instant) {
        return instant
                .atZone(ZoneId.of("Europe/Paris"))
                .toLocalDateTime();
    }

}
