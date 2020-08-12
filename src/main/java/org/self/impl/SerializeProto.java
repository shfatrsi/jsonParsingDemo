package org.self.impl;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.util.JsonFormat;
import org.self.impl.exceptions.SerializeException;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.function.Function;

public final class SerializeProto<TargetType> implements JsonSerialize<TargetType>
{
    @Override
    public void serialize(final OutputStream stream, final TargetType o) throws SerializeException
    {
        try
        {
            final OutputStreamWriter writer = new OutputStreamWriter(stream);
            printer.appendTo(function.apply(o), writer);
            writer.close();
        }
        catch(final IOException e)
        {
            throw new SerializeException(e);
        }
    }

    @Override
    public String serialize(final TargetType o) throws SerializeException
    {
        try
        {
            return printer.print(function.apply(o));
        }
        catch(final InvalidProtocolBufferException e)
        {
            throw new SerializeException(e);
        }
    }

    public static <TargetType> SerializeProto<TargetType> create(
        final JsonFormat.Printer printer,
        final Function<TargetType, MessageOrBuilder> function)
    {
        // TODO(shf): requireNonNull
        return new SerializeProto<>(printer, function);
    }

    private final JsonFormat.Printer printer;
    private final Function<TargetType, MessageOrBuilder> function;

    private SerializeProto(
        final JsonFormat.Printer printer,
        final Function<TargetType, MessageOrBuilder> function)
    {
        this.printer = printer;
        this.function = function;
    }

    // Cools stuff but won't make the final cut
    public static final class Builder<TargetType>
    {
        private JsonFormat.Printer printer;
        private Function<TargetType, MessageOrBuilder> function;

        public Builder<TargetType> printer(final JsonFormat.Printer value)
        {
            printer = value;
            return this;
        }

        public Builder<TargetType> function(final Function<TargetType, MessageOrBuilder> value)
        {
            function = value;
            return this;
        }

        public JsonSerialize<TargetType> build()
        {
            return new SerializeProto<>(this);
        }
    }

    private SerializeProto(final Builder<TargetType> builder)
    {
        this.printer = builder.printer;
        function = builder.function;
    }
}
