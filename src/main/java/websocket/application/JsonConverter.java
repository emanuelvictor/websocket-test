package websocket.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonConverter<T> {

    public T toEvent(final String json, final Class<T> tClass) {

        final ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.readValue(json, tClass);
        } catch (IOException e) {
            throw new RuntimeException("Invalid JSON:" + json, e);
        }
    }

    public String toJSON(T event) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
