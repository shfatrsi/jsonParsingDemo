package org.self.impl;

import java.io.OutputStream;

public interface JsonSerialize<T> {
    void serialize(OutputStream stream, T o) throws SerializeException;
}
