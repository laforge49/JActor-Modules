package org.agilewiki.jactor2.common.widgets.buffers;

import java.nio.ByteBuffer;

/**
 * A factory of unmodifiable byte buffers.
 */
public class UnmodifiableByteBufferFactory {
    private final ByteBuffer readOnly;

    public UnmodifiableByteBufferFactory(final byte[] _bytes) {
        byte[] bytes = new byte[_bytes.length];
        System.arraycopy(_bytes, 0, bytes, 0, bytes.length);
        readOnly = ByteBuffer.wrap(bytes).asReadOnlyBuffer();
    }

    public UnmodifiableByteBufferFactory(final ByteBuffer _byteBuffer) {
        int startPosition = _byteBuffer.position();
        byte[] bytes = new byte[_byteBuffer.limit() - startPosition];
        _byteBuffer.get(bytes);
        _byteBuffer.position(startPosition);
        readOnly = ByteBuffer.wrap(bytes).asReadOnlyBuffer();
    }

    public ByteBuffer duplicateByteBuffer() {
        return readOnly.duplicate();
    }

    TransmutableByteBuffer transmutableByteBuffer() {
        byte[] ba = new byte[readOnly.limit()];
        readOnly.get(ba);
        readOnly.rewind();
        return new TransmutableByteBuffer(ba);
    }
}
