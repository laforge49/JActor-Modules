package org.agilewiki.jactor2.common.widgets;

import org.agilewiki.jactor2.common.widgets.buffers.UnmodifiableByteBufferFactory;
import org.agilewiki.jactor2.core.blades.transmutable.Transmutable;

public interface Widget extends Transmutable<UnmodifiableByteBufferFactory> {
    /**
     * Returns the widget identified by the path.
     * The widget itself is returned when the path is empty.
     *
     * @param _path A set of keys with / as Separators.
     * @return The identified widget, or null.
     */
    Widget resolve(String _path);

    /**
     * Update the widget.
     *
     * @param _params         Defines the operation.
     * @param _contentFactory The unmodifiable data factory used in the operation.
     * @return A description of what happened.
     */
    String apply(final String _params, final String _contentType,
                 final UnmodifiableByteBufferFactory _contentFactory)
            throws WidgetException;

    /**
     * Returns the container widgit.
     *
     * @return The container widget, or null.
     */
    Widget getWidgetParent();

    /**
     * Returns the factory that created the widget.
     *
     * @return The factory.
     */
    WidgetFactory getWidgetFactory();

    /**
     * Create a deep copy.
     *
     * @return The copy.
     */
    Widget deepCopy();
}
