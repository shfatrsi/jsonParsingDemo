package org.self.impl;

import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.util.JsonFormat;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.function.Function;

public final class SerializeProto<TargetType> implements JsonSerialize<TargetType> {
    @Override
    public void serialize(final OutputStream stream, final TargetType o) throws SerializeException {
        try {
            final OutputStreamWriter writer = new OutputStreamWriter(stream);
            printer.appendTo(function.apply(o), writer);
            writer.close();
        } catch (final IOException e) {
            throw new SerializeException(e);
        }
    }

    private final JsonFormat.Printer printer;
    private final Function<TargetType, MessageOrBuilder> function;

    private SerializeProto(final Builder<TargetType> builder) {
        this.printer = builder.printer;
        function = builder.function;
    }

    public static final class Builder<T> {
        private JsonFormat.Printer printer;
        private Function<T, MessageOrBuilder> function;

        public Builder<T> printer(final JsonFormat.Printer value) {
            printer = value;
            return this;
        }

        public Builder<T> function(final Function<T, MessageOrBuilder> value) {
            function = value;
            return this;
        }

        public JsonSerialize<T> build() {
            return new SerializeProto<>(this);
        }
    }
}
