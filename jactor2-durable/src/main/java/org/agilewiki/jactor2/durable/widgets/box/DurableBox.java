package org.agilewiki.jactor2.durable.widgets.box;

import org.agilewiki.jactor2.common.widgets.InternalWidget;
import org.agilewiki.jactor2.durable.widgets.UnexpectedValueException;

public interface DurableBox {
    /**
     * Returns the factory key of the widget held by the box.
     *
     * @return The factory key, or the empty string.
     */
    String boxedFactoryKey();

    /**
     * Asserts the factory key.
     *
     * @param _expected    The expected factory key or the empty string.
     */
    void expectedFactoryKey(String _expected) throws UnexpectedValueException;

    /**
     * Returns the contents of the box.
     *
     * @return The contents of the box, or null.
     */
    InternalWidget boxedInternalWidget();

    /**
     * Empties the box.
     */
    void empty();

    /**
     * Returns true if the box is empty.
     *
     * @return True if the box is empty.
     */
    boolean isEmpty();

    /**
     * Makes a deep copy of a widget and puts that copy in the box.
     *
     * @param _internalWidget    The widget to be copied.
     */
    void putCopy(InternalWidget _internalWidget);
}
