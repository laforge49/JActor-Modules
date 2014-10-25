package org.agilewiki.jactor2.durable.widgets.box;

import org.agilewiki.jactor2.common.widgets.Widget;
import org.agilewiki.jactor2.durable.widgets.InvalidWidgetContentException;
import org.agilewiki.jactor2.durable.widgets.UnexpectedValueException;

public interface DurableBox extends Widget {
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
    Widget getContent();

    /**
     * Makes a deep copy of a widget and puts that copy in the box.
     *
     * @param _widget The widget to be copied.
     */
    void putCopy(Widget _widget) throws InvalidWidgetContentException;
}
