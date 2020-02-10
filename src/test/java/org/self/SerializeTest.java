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

public final class SerializeTest {
    @Test
    public void protoSerialize() throws Exception {
        serialize(new SerializeProto.Builder<TestExample>()
                        .printer(JsonFormat.printer()
                                .omittingInsignificantWhitespace()
                                .includingDefaultValueFields())
                        .function(testExample -> User.newBuilder()
                                .setMessage(testExample.getMessage())
                                .setCode(testExample.getCode())
                                .setStatus(testExample.isStatus())
                                .build())
                        .build(),
                new ByteArrayOutputStream(),
                new DeserializeProto.Builder<TestExample, User.Builder>()
                        .parser(JsonFormat.parser()
                                .ignoringUnknownFields())
                        .supplier(User::newBuilder)
                        .function(user -> TestExample.builder()
                                .status(user.getStatus())
                                .code(user.getCode())
                                .message(user.getMessage())
                                .build())
                        .build());
//                new DeserializeProto<>(
//                        JsonFormat.parser()
//                                .ignoringUnknownFields(),
//                        User::newBuilder,
//                        user -> TestExample.builder()
//                                .status(user.getStatus())
//                                .code(user.getCode())
//                                .message(user.getMessage())
//                                .build()));
    }

    @Test
    public void jacksonSerialize() throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        serialize(new SerializeJackson.Builder<TestExample>()
                        .objectMapper(objectMapper)
                        .build(),
                new ByteArrayOutputStream(),
                new DeserializeJackson.Builder<TestExample>()
                        .objectMapper(objectMapper)
                        .classType(TestExample.class)
                        .build());
//        new DeserializeJackson<>(objectMapper, TestExample.class));
//        serialize(new SerializeJackson<>(objectMapper),
//                new ByteArrayOutputStream(),
//                new DeserializeJackson<>(objectMapper, TestExample.class));
    }

    private void serialize(
            final JsonSerialize<TestExample> serializer,
            final ByteArrayOutputStream stream,
            final JsonDeserialize<TestExample> deserializer) throws Exception {
        serializer.serialize(stream, TestExample.builder()
                .message("hiya")
                .status(true)
                .code(23)
                .build());

        final ByteArrayInputStream inputStream = new ByteArrayInputStream(stream.toByteArray());
        final TestExample roundTripped = deserializer.deserialize(inputStream);

        assertThat(roundTripped)
                .isEqualTo(TestExample.builder()
                        .message("hiya")
                        .code(23)
                        .status(true)
                        .build());
    }

    @Test
    public void protoSerializeException() {
    }

    @Test
    public void jacksonSerializeException() {
    }
}
