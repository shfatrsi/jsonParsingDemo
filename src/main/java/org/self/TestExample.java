package org.self;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@JsonDeserialize(builder = TestExample.TestExampleBuilder.class)
@Builder(toBuilder = true)
@Value
public class TestExample
{
    String message;
    int code;
    boolean status;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class TestExampleBuilder
    {
    }
}
