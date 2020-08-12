package org.self;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.util.JsonFormat;
import org.junit.Test;
import org.self.impl.DeserializeJackson;
import org.self.impl.DeserializeProto;
import org.self.impl.JsonDeserialize;
import org.self.impl.JsonSerialize;
import org.self.impl.SerializeJackson;
import org.self.impl.SerializeProto;
import org.self.impl.User;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.assertj.core.api.Assertions.assertThat;

public final class SerializeTest
{
    @Test
    public void protoSerializeJacksonClass() throws Exception
    {
        final TestExample roundTripped = roundTrip(
            SerializeProto.create(
                JsonFormat.printer()
                    .omittingInsignificantWhitespace()
                    .includingDefaultValueFields(),
                testExample -> User.newBuilder()
                    .setMessage(testExample.getMessage())
                    .setCode(testExample.getCode())
                    .setStatus(testExample.isStatus())
                    .build()),
            DeserializeProto.create(
                JsonFormat.parser()
                    .ignoringUnknownFields(),
                User::newBuilder,
                user -> TestExample.builder()
                    .status(user.getStatus())
                    .code(user.getCode())
                    .message(user.getMessage())
                    .build()),
            TestExample.builder()
                .message("hiya")
                .status(true)
                .code(23)
                .build());

        assertThat(roundTripped).isEqualTo(
            TestExample.builder()
                .message("hiya")
                .status(true)
                .code(23)
                .build());
    }

    @Test
    public void jacksonSerializeJacksonClass() throws Exception
    {
        final ObjectMapper objectMapper = new ObjectMapper()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        final TestExample roundTripped = roundTrip(
            SerializeJackson.create(objectMapper),
            DeserializeJackson.create(objectMapper, TestExample.class),
            TestExample.builder()
                .message("hiya")
                .status(true)
                .code(23)
                .build());

        assertThat(roundTripped).isEqualTo(
            TestExample.builder()
                .message("hiya")
                .status(true)
                .code(23)
                .build());
    }

    private <T> T roundTrip(
        final JsonSerialize<T> serializer,
        final JsonDeserialize<T> deserializer,
        final T o) throws Exception
    {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();

        serializer.serialize(stream, o);

        final ByteArrayInputStream inputStream = new ByteArrayInputStream(stream.toByteArray());
        return deserializer.deserialize(inputStream);
    }

    @Test
    public void protoSerializeException()
    {
    }

    @Test
    public void jacksonSerializeException()
    {
    }
}
