package org.agilewiki.jactor2.common.widgets.buffers;

import java.nio.ByteBuffer;

/**
 * A factory of unmodifiable byte buffers.
 */
public class UnmodifiableByteBufferFactory {
    private final byte[] bytes;
    private final ByteBuffer readOnly;

    public UnmodifiableByteBufferFactory(final byte[] _bytes) {
        bytes = new byte[_bytes.length];
        System.arraycopy(_bytes, 0, bytes, 0, bytes.length);
        readOnly = ByteBuffer.wrap(bytes).asReadOnlyBuffer();
    }

    public ByteBuffer duplicateByteBuffer() {
        return readOnly.duplicate();
    }

    TransmutableByteBuffer transmutableByteBuffer() {
        byte[] ba = new byte[bytes.length];
        System.arraycopy(bytes, 0, ba, 0, bytes.length);
        return new TransmutableByteBuffer(ba);
    }
}
