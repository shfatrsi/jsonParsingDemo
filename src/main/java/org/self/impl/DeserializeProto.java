package org.self.impl;

import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Function;
import java.util.function.Supplier;

public class DeserializeProto<TargetType, BuilderType extends Message.Builder> implements JsonDeserialize<TargetType> {
    @Override
    public TargetType deserialize(final InputStream stream) throws DeserializeException {
        try {
            final BuilderType builderType = supplier.get();
            parser.merge(new InputStreamReader(stream), builderType);
            return function.apply(builderType);
        } catch (final IOException e) {
            throw new DeserializeException(e);
        }
    }

    private final JsonFormat.Parser parser;
    private final Supplier<BuilderType> supplier;
    private final Function<BuilderType, TargetType> function;

    private DeserializeProto(final Builder<TargetType, BuilderType> builder) {
        parser = builder.parser;
        supplier = builder.supplier;
        function = builder.function;
    }

    public final static class Builder<T, B extends Message.Builder> {
        private JsonFormat.Parser parser;
        private Supplier<B> supplier;
        private Function<B, T> function;

        public Builder<T, B> parser(final JsonFormat.Parser value) {
            parser = value;
            return this;
        }

        public Builder<T, B> supplier(final Supplier<B> value) {
            supplier = value;
            return this;
        }

        public Builder<T, B> function(final Function<B, T> value) {
            function = value;
            return this;
        }

        public JsonDeserialize<T> build() {
            return new DeserializeProto<>(this);
        }
    }
}
