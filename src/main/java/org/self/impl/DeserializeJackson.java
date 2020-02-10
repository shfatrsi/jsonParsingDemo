package org.self.impl;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class DeserializeJackson<TargetType> implements JsonDeserialize<TargetType> {
    @Override
    public TargetType deserialize(final InputStream stream) throws DeserializeException {
        try {
            return objectMapper.readValue(stream, classType);
            // TODO(shf): Catch sub exceptions
        } catch (final IOException e) {
            throw new DeserializeException(e);
        }
    }

    private final ObjectMapper objectMapper;
    private final Class<TargetType> classType;

    private DeserializeJackson(final Builder<TargetType> builder) {
        this.objectMapper = builder.objectMapper;
        this.classType = builder.classType;
    }

    public final static class Builder<T> {
        private ObjectMapper objectMapper;
        private Class<T> classType;

        public Builder<T> objectMapper(final ObjectMapper value) {
            objectMapper = value;
            return this;
        }

        public Builder<T> classType(final Class<T> value) {
            classType = value;
            return this;
        }

        public JsonDeserialize<T> build() {
            return new DeserializeJackson<>(this);
        }
    }
}
