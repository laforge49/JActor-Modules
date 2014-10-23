package org.agilewiki.jactor2.durable.widgets.string;

import org.agilewiki.jactor2.durable.widgets.Durable;
import org.agilewiki.jactor2.durable.widgets.InvalidWidgetContentException;
import org.agilewiki.jactor2.durable.widgets.UnexpectedValueException;

/**
 * A serializable string.
 */
public interface DurableString extends Durable {

    /**
     * Returns the value.
     *
     * @return The value.
     */
    String getValue() throws Exception;

    /**
     * Returns the string length.
     *
     * @return The string length.
     */
    int length();

    /**
     * Assigns a value.
     *
     * @param _value The new value.
     */
    void setValue(final String _value) throws InvalidWidgetContentException;

    /**
     * Asserts a value.
     *
     * @param _value The expected value.
     */
    void expect(final String _value) throws UnexpectedValueException;
}
