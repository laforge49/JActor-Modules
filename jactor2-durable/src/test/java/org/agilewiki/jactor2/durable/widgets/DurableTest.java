package org.agilewiki.jactor2.durable.widgets;

import junit.framework.TestCase;
import org.agilewiki.jactor2.common.CFacility;
import org.agilewiki.jactor2.common.CPlant;

import java.nio.ByteBuffer;

public class DurableTest extends TestCase {
    public void test1() throws Exception {
        new CPlant();
        try {
            CFacility facility = (CFacility) CPlant.getInternalFacility();
            facility.addWidgetFactorySOp(new DurableFactory("durable", facility)).call();
            DurableImpl durableImpl = (DurableImpl) facility.
                    newWidgetImpl("durable", null, null);
            ByteBuffer buffer = durableImpl.createUnmodifiable().duplicateByteBuffer();
            assertEquals(0, buffer.limit());
        } finally {
            CPlant.close();
        }
    }
}
