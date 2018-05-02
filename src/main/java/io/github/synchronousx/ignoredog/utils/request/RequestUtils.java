package io.github.synchronousx.ignoredog.utils.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class RequestUtils {
    public static final ObjectMapper JACKSON_OBJECT_MAPPER = new ObjectMapper();
    public static final com.mashape.unirest.http.ObjectMapper UNIREST_OBJECT_MAPPER = new com.mashape.unirest.http.ObjectMapper() {
        @Override
        public <T> T readValue(final String value, final Class<T> valueType) {
            try {
                return RequestUtils.JACKSON_OBJECT_MAPPER.readValue(value, valueType);
            } catch (final IOException exception) {
                throw new RequestException(exception);
            }
        }

        @Override
        public String writeValue(final Object value) {
            try {
                return RequestUtils.JACKSON_OBJECT_MAPPER.writeValueAsString(value);
            } catch (final JsonProcessingException exception) {
                throw new RequestException(exception);
            }
        }
    };

    private RequestUtils() {
    }

    public static class RequestException extends RuntimeException {
        public RequestException() {
            super();
        }

        public RequestException(final String message) {
            super(message);
        }

        public RequestException(final String message, final Throwable cause) {
            super(message, cause);
        }

        public RequestException(final Throwable cause) {
            super(cause);
        }

        protected RequestException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }
}
