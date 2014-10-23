package org.agilewiki.jactor2.common.widgets;

import org.agilewiki.jactor2.common.widgets.buffers.UnmodifiableByteBufferFactory;

public interface Widget {
    /**
     * Returns the widget identified by the path.
     * The widget itself is returned when the path is empty.
     *
     * @param _path A set of keys with / as Separators.
     * @return The identified widget.
     */
    Widget resolve(String _path) throws InvalidWidgetPathException;

    /**
     * Update the widget.
     *
     * @param _params         Defines the operation.
     * @param _contentFactory The unmodifiable data used in the operation.
     * @return A description of what happened.
     */
    String apply(final String _params,
                 final UnmodifiableByteBufferFactory _contentFactory)
            throws Exception;
}
