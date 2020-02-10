package org.self.impl;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;

public final class SerializeJackson<TargetType> implements JsonSerialize<TargetType> {
    @Override
    public void serialize(final OutputStream stream, final TargetType o) throws SerializeException {
        try {
            objectMapper.writeValue(stream, o);
        } catch (final IOException e) {
            throw new SerializeException(e);
        }
    }

    private final ObjectMapper objectMapper;

    private SerializeJackson(final Builder<TargetType> builder) {
        objectMapper = builder.objectMapper;
    }

    public static final class Builder<T> {
        private ObjectMapper objectMapper;

        public Builder<T> objectMapper(final ObjectMapper value) {
            objectMapper = value;
            return this;
        }

        public JsonSerialize<T> build() {
            return new SerializeJackson<>(this);
        }
    }
}
