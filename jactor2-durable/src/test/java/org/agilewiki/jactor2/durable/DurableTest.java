package org.agilewiki.jactor2.durable;

import junit.framework.TestCase;
import org.agilewiki.jactor2.common.CFacility;
import org.agilewiki.jactor2.common.CPlant;
import org.agilewiki.jactor2.common.widgets.WidgetImpl;
import org.agilewiki.jactor2.common.widgets.buffers.UnmodifiableByteBufferFactory;
import org.agilewiki.jactor2.durable.widgets.DurableFactory;
import org.agilewiki.jactor2.durable.widgets.DurableImpl;

public class DurableTest extends TestCase {
    public void test1() throws Exception {
        new CPlant();
        try {
            CFacility facility = (CFacility) CPlant.getInternalFacility();
            facility.addWidgetFactorySOp(new DurableFactory("test", facility)).call();
            WidgetImpl<UnmodifiableByteBufferFactory> widgitImpl = facility.
                    newWidgetImpl("test", null, null);
            DurableImpl durableImpl = (DurableImpl) widgitImpl;
            System.out.println(durableImpl);
        } finally {
            CPlant.close();
        }
    }
}
