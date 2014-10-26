package org.agilewiki.jactor2.durable.widgets.string;

import junit.framework.TestCase;
import org.agilewiki.jactor2.common.CFacility;
import org.agilewiki.jactor2.common.CPlant;
import org.agilewiki.jactor2.common.widgets.WidgetException;
import org.agilewiki.jactor2.durable.transactions.DurableReference;
import org.agilewiki.jactor2.durable.transactions.DurableTransaction;

import java.nio.ByteBuffer;

public class StringTest extends TestCase {
    public void test1() throws Exception {
        new CPlant();
        try {
            CFacility facility = (CFacility) CPlant.getInternalFacility();
            StringFactory.addFactorySOp(facility).call();

            DurableString dStr = StringFactory.newDurableString(facility, null);
            ByteBuffer buffer = dStr.createUnmodifiable().duplicateByteBuffer();
            assertEquals(4, buffer.limit());
            assertEquals("", StringFactory.readString(buffer.duplicate()));
            assertEquals(0, dStr.length());
            assertEquals("", dStr.getValue());

            dStr.setValue("1");
            assertEquals(1, dStr.length());
            assertEquals("1", dStr.getValue());
            ByteBuffer buffer1 = dStr.createUnmodifiable().duplicateByteBuffer();
            assertEquals(6, buffer1.limit());
            assertEquals("1", StringFactory.readString(buffer1.duplicate()));

            DurableString dstr2 = StringFactory.newDurableString(facility, "-");
            assertEquals("-", dstr2.getValue());

            DurableString dstr3 = (DurableString) facility.
                    newWidget(StringFactory.FACTORY_NAME, buffer1);
            assertEquals("1", dstr3.getValue());

            DurableTransaction setTrans = StringFactory.setValueTransaction(facility, "", "42");
            DurableReference durableReference = new DurableReference(dstr3);
            durableReference.applyAOp(setTrans).call();
            assertEquals("42", dstr3.getValue());
            System.out.println(setTrans.toString());

            DurableTransaction expectTrans = StringFactory.expectTransaction(facility, "", "43");
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
