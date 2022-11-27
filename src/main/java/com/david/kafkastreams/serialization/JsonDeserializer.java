package com.david.kafkastreams.serialization;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

/**
 * Generic JsonDeserializer
 * @param <T> any Type to be deserialized from bytes to POJO
 */
public class JsonDeserializer<T> implements Deserializer<T> {

    private final ObjectMapper mapper = new ObjectMapper();
    private Class<T> targetClass;

    public JsonDeserializer() {
    }

    public JsonDeserializer(Class<T> targetClass) {
        this.targetClass = targetClass;
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        if (data == null)
            return null;
        try {
            return mapper.readValue(data, targetClass);
        } catch (Exception e) {
            throw new SerializationException("Error deserializing message", e);
        }
    }
}
