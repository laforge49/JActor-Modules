package org.agilewiki.jactor2.common.widgets.buffers;

import org.agilewiki.jactor2.core.blades.transmutable.Transmutable;

import java.nio.ByteBuffer;

/**
 * A transmutable wrapper of a byte buffer.
 */
public class TransmutableByteBuffer implements Transmutable<UnmodifiableByteBufferFactory> {
    public final ByteBuffer byteBuffer;

    public TransmutableByteBuffer(final byte[] _bytes) {
        byteBuffer = ByteBuffer.wrap(_bytes);
    }

    public TransmutableByteBuffer(final int _length) {
        byteBuffer = ByteBuffer.allocate(_length);
    }

    public TransmutableByteBuffer(final ByteBuffer _byteBuffer) {
        byteBuffer = _byteBuffer;
    }

    @Override
    public UnmodifiableByteBufferFactory createUnmodifiable() {
        return new UnmodifiableByteBufferFactory(byteBuffer.array());
    }

    @Override
    public Transmutable recreate(UnmodifiableByteBufferFactory unmodifiable) {
        return new TransmutableByteBuffer(unmodifiable.duplicateByteBuffer());
    }
}
