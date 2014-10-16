package org.agilewiki.jactor2.durable.widgets.integer;

import junit.framework.TestCase;
import org.agilewiki.jactor2.common.CFacility;
import org.agilewiki.jactor2.common.CPlant;

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
            DurableInt dint2 = (DurableInt) facility.
                    newWidgetImpl("int", null, buffer).asWidget();
            assertEquals(0, dint2.getValue().intValue());
            DurableInt dint3 = (DurableInt) facility.
                    newWidgetImpl("int", null, buffer1).asWidget();
            assertEquals(1, dint3.getValue().intValue());
        } finally {
            CPlant.close();
        }
    }
}
