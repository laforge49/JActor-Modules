package org.agilewiki.jactor2.durable.widgets.box;

import org.agilewiki.jactor2.common.widgets.InternalWidget;
import org.agilewiki.jactor2.durable.widgets.UnexpectedValueException;

public interface DurableBox {
    /**
     * Returns the factory key of the widget held by the box.
     *
     * @return The factory key.
     */
    String boxedFactoryKey();

    /**
     * Asserts the factory key.
     *
     * @param _expected The expected factory key or the empty string.
     */
    void expectedFactoryKey(String _expected) throws UnexpectedValueException;

    /**
     * Returns the contents of the box.
     *
     * @return The contents of the box.
     */
    InternalWidget getBoxedInternalWidget();

    /**
     * Makes a deep copy of a widget and puts that copy in the box.
     *
     * @param _internalWidget The widget to be copied.
     */
    void putCopy(InternalWidget _internalWidget);
}
