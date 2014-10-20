package org.agilewiki.jactor2.durable.widgets.integer;

import org.agilewiki.jactor2.durable.widgets.Durable;
import org.agilewiki.jactor2.durable.widgets.InvalidDurableContentException;
import org.agilewiki.jactor2.durable.widgets.UnexpectedValueException;

/**
 * Serializable Integer.
 */
public interface DurableInt extends Durable {

    /**
     * Returns the value.
     *
     * @return The value.
     */
    Integer getValue() throws Exception;

    /**
     * Assigns a value.
     *
     * @param _value The new value.
     */
    void setValue(final Integer _value) throws InvalidDurableContentException;

    /**
     * Asserts a value.
     *
     * @param _value The expected value.
     */
    void expect(final Integer _value) throws UnexpectedValueException;
}
