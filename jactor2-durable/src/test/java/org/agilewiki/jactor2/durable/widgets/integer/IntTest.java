package org.agilewiki.jactor2.durable.widgets.integer;

import junit.framework.TestCase;
import org.agilewiki.jactor2.common.CFacility;
import org.agilewiki.jactor2.common.CPlant;
import org.agilewiki.jactor2.common.widgets.WidgetException;
import org.agilewiki.jactor2.durable.transactions.DurableReference;
import org.agilewiki.jactor2.durable.transactions.DurableTransaction;

import java.nio.ByteBuffer;

public class IntTest extends TestCase {
    public void test1() throws Exception {
        new CPlant();
        try {
            CFacility facility = (CFacility) CPlant.getInternalFacility();
            IntFactory.addFactorySOp(facility).call();

            DurableInt dint = IntFactory.newDurableInt(facility, null);
            ByteBuffer buffer = dint.createUnmodifiable().duplicateByteBuffer();
            assertEquals(4, buffer.limit());
            assertEquals(0, buffer.duplicate().getInt());
            assertEquals(Integer.valueOf(0), dint.getValue());

            dint.setValue(1);
            assertEquals(Integer.valueOf(1), dint.getValue());
            ByteBuffer buffer1 = dint.createUnmodifiable().duplicateByteBuffer();
            assertEquals(4, buffer1.limit());
            assertEquals(1, buffer1.duplicate().getInt());

            DurableInt dint2 = IntFactory.newDurableInt(facility, 0);
            assertEquals(Integer.valueOf(0), dint2.getValue());

            IntImpl intImpl3 = (IntImpl) facility.
                    newInternalWidget(IntFactory.FACTORY_NAME, null, buffer1);
            DurableInt dint3 = intImpl3.asWidget();
            assertEquals(Integer.valueOf(1), dint3.getValue());

            DurableTransaction setTrans = IntImpl.setValueTransaction(facility, "", 42);
            DurableReference durableReference = new DurableReference(dint3);
            durableReference.applyAOp(setTrans).call();
            assertEquals(Integer.valueOf(42), dint3.getValue());
            System.out.println(setTrans.toString());

            DurableTransaction expectTrans = IntImpl.expectTransaction(facility, "", 43);
            try {
                durableReference.applyAOp(expectTrans).call();
            } catch (WidgetException ide) {
                System.out.println(ide);
            }
        } finally {
            CPlant.close();
        }
    }
}
