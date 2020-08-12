package org.self.impl;

import org.self.impl.exceptions.SerializeException;

import java.io.OutputStream;

public interface JsonSerialize<T>
{
    void serialize(OutputStream stream, T o) throws SerializeException;

    String serialize(T o) throws SerializeException;
}
