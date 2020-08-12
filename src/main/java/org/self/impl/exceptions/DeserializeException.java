package org.self.impl.exceptions;

public class DeserializeException extends Exception
{
    public DeserializeException(final Exception e)
    {
        super(e);
    }
}
