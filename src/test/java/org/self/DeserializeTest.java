package org.self;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.util.JsonFormat;
import org.junit.Test;
import org.self.impl.DeserializeException;
import org.self.impl.DeserializeJackson;
import org.self.impl.DeserializeProto;
import org.self.impl.JsonDeserialize;
import org.self.impl.User;

import java.io.ByteArrayInputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public final class DeserializeTest {
    @Test
    public void protoDeserialize() throws Exception {
        deserialize(new DeserializeProto.Builder<TestExample, User.Builder>()
                        .parser(JsonFormat.parser()
                                .ignoringUnknownFields())
                        .supplier(User::newBuilder)
                        .function(user -> TestExample.builder()
                                .status(user.getStatus())
                                .code(user.getCode())
                                .message(user.getMessage())
                                .build())
                        .build(),
                "{\"code\":23,\"message\":\"hiya\",\"status\":true}");
    }

    @Test
    public void jacksonDeserialize() throws Exception {
        deserialize(new DeserializeJackson.Builder<TestExample>()
                        .objectMapper(new ObjectMapper()
                                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES))
                        .classType(TestExample.class)
                        .build(),
                "{\"code\":23,\"message\":\"hiya\",\"status\":true}");
    }

    private void deserialize(final JsonDeserialize<TestExample> deserializer, final String json) throws DeserializeException {
        final ByteArrayInputStream stream = new ByteArrayInputStream(json.getBytes());

        assertThat(deserializer.deserialize(stream))
                .isEqualTo(TestExample.builder()
                        .message("hiya")
                        .code(23)
                        .status(true)
                        .build());
    }

    @Test
    public void protoDeserializeException() {
        assertThatThrownBy(() -> deserialize(new DeserializeProto.Builder<TestExample, User.Builder>()
                        .parser(JsonFormat.parser())
                        .supplier(User::newBuilder)
                        .function(user -> TestExample.builder()
                                .build())
                        .build(),
                ""))
//        new DeserializeProto<>(
//                JsonFormat.parser(),
//                User::newBuilder,
//                user -> TestExample.builder()
//                        .build()),
//                ""))
                .isInstanceOf(DeserializeException.class)
                .hasMessage("com.google.protobuf.InvalidProtocolBufferException: " +
                        "Expect message object but got: null");
    }

    @Test
    public void jacksonDeserializeException() {
        assertThatThrownBy(() -> deserialize(new DeserializeJackson.Builder<TestExample>()
                        .objectMapper(new ObjectMapper())
                        .classType(TestExample.class)
                        .build(),
                ""))
                .isInstanceOf(DeserializeException.class)
                .hasMessage("com.fasterxml.jackson.databind.exc.MismatchedInputException: " +
                        "No content to map due to end-of-input\n " +
                        "at [Source: (ByteArrayInputStream); line: 1, column: 0]");
    }
}
