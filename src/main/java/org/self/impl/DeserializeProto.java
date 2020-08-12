package org.self.impl;

import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import org.self.impl.exceptions.DeserializeException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public final class DeserializeProto<TargetType, BuilderType extends Message.Builder>
    implements JsonDeserialize<TargetType>
{
    @Override
    public TargetType deserialize(final InputStream stream) throws DeserializeException
    {
        try(final InputStreamReader streamReader = new InputStreamReader(stream))
        {
            final BuilderType builderType = supplier.get();
            parser.merge(streamReader, builderType);
            return function.apply(builderType);
        }
        catch(final IOException e)
        {
            throw new DeserializeException(e);
        }
    }

    public static <TargetType, BuilderType extends Message.Builder> DeserializeProto<TargetType, BuilderType> create(
        final JsonFormat.Parser parser,
        final Supplier<BuilderType> supplier,
        final Function<BuilderType, TargetType> function)
    {
        return new DeserializeProto<>(
            requireNonNull(parser, "parser"),
            requireNonNull(supplier, "supplier"),
            requireNonNull(function, "function"));
    }

    private final JsonFormat.Parser parser;
    private final Supplier<BuilderType> supplier;
    private final Function<BuilderType, TargetType> function;

    private DeserializeProto(
        final JsonFormat.Parser parser,
        final Supplier<BuilderType> supplier,
        final Function<BuilderType, TargetType> function)
    {
        this.parser = parser;
        this.supplier = supplier;
        this.function = function;
    }

    // This was a fun test but the factory of method is a little
    //  better for this class I feel. If any more params got added
    public final static class Builder<TargetType, BuilderType extends Message.Builder>
    {
        private JsonFormat.Parser parser;
        private Supplier<BuilderType> supplier;
        private Function<BuilderType, TargetType> function;

        public static <TargetType, BuilderType extends Message.Builder> Builder<TargetType, BuilderType> builder()
        {
            return new Builder<>();
        }

        public Builder<TargetType, BuilderType> parser(final JsonFormat.Parser value)
        {
            parser = value;
            return this;
        }

        public Builder<TargetType, BuilderType> supplier(final Supplier<BuilderType> value)
        {
            supplier = value;
            return this;
        }

        public Builder<TargetType, BuilderType> function(final Function<BuilderType, TargetType> value)
        {
            function = value;
            return this;
        }

        public JsonDeserialize<TargetType> build()
        {
            return new DeserializeProto<>(this);
        }
    }

    private DeserializeProto(final Builder<TargetType, BuilderType> builder)
    {
        parser = builder.parser;
        supplier = builder.supplier;
        function = builder.function;
    }
}
