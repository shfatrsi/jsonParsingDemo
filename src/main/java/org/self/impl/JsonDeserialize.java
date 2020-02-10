package org.self.impl;

import java.io.InputStream;

public interface JsonDeserialize<T> {
    T deserialize(InputStream stream) throws DeserializeException;
}
