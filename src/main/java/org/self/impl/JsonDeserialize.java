package org.self.impl;

import org.self.impl.exceptions.DeserializeException;

import java.io.InputStream;

public interface JsonDeserialize<T>
{
    T deserialize(InputStream stream) throws DeserializeException;
}
