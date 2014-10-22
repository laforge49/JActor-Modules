package org.agilewiki.jactor2.common.widgets;

import org.agilewiki.jactor2.common.widgets.buffers.UnmodifiableByteBufferFactory;
import org.agilewiki.jactor2.core.blades.transmutable.Transmutable;

import java.nio.ByteBuffer;

public interface InternalWidget extends Transmutable<UnmodifiableByteBufferFactory> {

    /**
     * Returns the size of the serialized data.
     *
     * @return The number of bytes.
     */
    int getBufferSize();

    /**
     * Signals the change of a child.
     *
     * @param _delta The difference in size of the serialized data.
     */
    void childChange(int _delta);

    /**
     * Serialize to a ByteBuffer, starting at the current position.
     * Once written, the content should not change (append only), as a view
     * of the content is retained.
     *
     * @param _byteBuffer A ByteBuffer to be updated.
     */
    void serialize(final ByteBuffer _byteBuffer);

    /**
     * Returns the factory that created the widget.
     *
     * @return The factory.
     */
    InternalWidgetFactory getInternalWidgetFactory();

    /**
     * Clear the parent reference.
     */
    void clearParent();

    /**
     * Create a deep copy.
     *
     * @return The copy.
     */
    WidgetImpl deepCopy();
}
