package org.self.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.self.impl.exceptions.DeserializeException;

import java.io.IOException;
import java.io.InputStream;

public class DeserializeJackson<TargetType> implements JsonDeserialize<TargetType>
{
    @Override
    public TargetType deserialize(final InputStream stream) throws DeserializeException
    {
        try
        {
            return objectMapper.readValue(stream, classType);
            // TODO(shf): Handle sub exceptions?
        }
        catch(final IOException e)
        {
            throw new DeserializeException(e);
        }
    }

    public static <TargetType> DeserializeJackson<TargetType> create(
        final ObjectMapper objectMapper,
        final Class<TargetType> classType)
    {
        // TODO(shf): requireNonNull
        return new DeserializeJackson<>(objectMapper, classType);
    }

    private final ObjectMapper objectMapper;
    private final Class<TargetType> classType;

    private DeserializeJackson(
        final ObjectMapper objectMapper,
        final Class<TargetType> classType)
    {
        this.objectMapper = objectMapper;
        this.classType = classType;
    }

    // Builder has been fun but this won't make it beyond the real deal

//    public final static class Builder<T>
//    {
//        private ObjectMapper objectMapper;
//        private Class<T> classType;
//
//        public Builder<T> objectMapper(final ObjectMapper value)
//        {
//            objectMapper = value;
//            return this;
//        }
//
//        public Builder<T> classType(final Class<T> value)
//        {
//            classType = value;
//            return this;
//        }
//
//        public JsonDeserialize<T> build()
//        {
//            return new DeserializeJackson<>(this);
//        }
//    }
//
//    private DeserializeJackson(final Builder<TargetType> builder)
//    {
//        this.objectMapper = builder.objectMapper;
//        this.classType = builder.classType;
//    }
}
