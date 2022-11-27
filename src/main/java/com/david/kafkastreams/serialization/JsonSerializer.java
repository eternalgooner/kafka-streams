package com.david.kafkastreams.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

/**
 * Custome JsonSerializer
 * @param <T> any Type to be serialized from POJO to bytes
 */
public class JsonSerializer<T> implements Serializer<T> {
    private final ObjectMapper mapper = new ObjectMapper();

    private Class<T> targetClass;

    public JsonSerializer() {
    }

    public JsonSerializer(Class<T> targetClass) {
        this.targetClass = targetClass;
    }

    @Override
    public byte[] serialize(String topic, Object data) {
        if (data == null)
            return null;
        try {
            return mapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new SerializationException("Error serializing JSON message", e);
        }
    }
}
