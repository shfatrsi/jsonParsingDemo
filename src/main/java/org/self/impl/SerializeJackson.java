package org.self.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.self.impl.exceptions.SerializeException;

import java.io.IOException;
import java.io.OutputStream;

public final class SerializeJackson<TargetType> implements JsonSerialize<TargetType>
{
    @Override
    public void serialize(final OutputStream stream, final TargetType o) throws SerializeException
    {
        try
        {
            objectMapper.writeValue(stream, o);
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
            return objectMapper.writeValueAsString(o);
        }
        catch(final IOException e)
        {
            throw new SerializeException(e);
        }
    }

    public static <TargetType> SerializeJackson<TargetType> create(
        final ObjectMapper objectMapper)
    {
        // TODO(shf): requireNonNull
        return new SerializeJackson<>(objectMapper);
    }

    private final ObjectMapper objectMapper;

    private SerializeJackson(final ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    // This is cool but not making the final cut

//    public static final class Builder<TargetType>
//    {
//        private ObjectMapper objectMapper;
//
//        public Builder<TargetType> objectMapper(final ObjectMapper value)
//        {
//            objectMapper = value;
//            return this;
//        }
//
//        public JsonSerialize<TargetType> build()
//        {
//            return new SerializeJackson<>(this);
//        }
//    }
//
//    private SerializeJackson(final Builder<TargetType> builder)
//    {
//        objectMapper = builder.objectMapper;
//    }
}
