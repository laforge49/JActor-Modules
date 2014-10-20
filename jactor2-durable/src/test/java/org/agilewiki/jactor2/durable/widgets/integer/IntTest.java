package org.agilewiki.jactor2.durable.widgets.integer;

import junit.framework.TestCase;
import org.agilewiki.jactor2.common.CFacility;
import org.agilewiki.jactor2.common.CPlant;
import org.agilewiki.jactor2.common.widgets.buffers.UnmodifiableByteBufferFactory;
import org.agilewiki.jactor2.durable.transactions.DurableReference;
import org.agilewiki.jactor2.durable.transactions.DurableTransaction;

import java.nio.ByteBuffer;

public class IntTest extends TestCase {
    public void test1() throws Exception {
        new CPlant();
        try {
            CFacility facility = (CFacility) CPlant.getInternalFacility();
            facility.addWidgetFactorySOp(new IntFactory("int", facility)).call();
            IntImpl intImpl = (IntImpl) facility.
                    newWidgetImpl("int", null, null);
            ByteBuffer buffer = intImpl.createUnmodifiable().duplicateByteBuffer();
            assertEquals(4, buffer.limit());
            assertEquals(0, buffer.getInt());
            buffer.rewind();
            DurableInt dint = intImpl.asWidget();
            assertEquals(0, dint.getValue().intValue());
            dint.setValue(1);
            assertEquals(1, dint.getValue().intValue());
            ByteBuffer buffer1 = intImpl.createUnmodifiable().duplicateByteBuffer();
            assertEquals(4, buffer1.limit());
            assertEquals(1, buffer1.getInt());
            buffer1.rewind();
            IntImpl intImpl2 = (IntImpl) facility.newWidgetImpl("int", null, buffer);
            DurableInt dint2 = intImpl2.asWidget();
            assertEquals(0, dint2.getValue().intValue());
            IntImpl intImpl3 = (IntImpl) facility.
                    newWidgetImpl("int", null, buffer1);
            DurableInt dint3 = intImpl3.asWidget();
            assertEquals(1, dint3.getValue().intValue());

            ByteBuffer bb42 = ByteBuffer.allocate(4);
            bb42.putInt(42).rewind();
            DurableTransaction setTrans = new DurableTransaction("", "setValue",
                    new UnmodifiableByteBufferFactory(bb42));
            DurableReference durableReference = new DurableReference(intImpl3);
            durableReference.applyAOp(setTrans).call();
            assertEquals(42, dint3.getValue().intValue());
            System.out.println(setTrans.toString());
        } finally {
            CPlant.close();
        }
    }
}
