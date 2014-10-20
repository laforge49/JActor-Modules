package org.agilewiki.jactor2.durable.widgets;

public class UnexpectedValueException extends InvalidDurableException {
    public UnexpectedValueException(final String _msg) {
        super(_msg);
    }
}
