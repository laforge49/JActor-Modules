package org.agilewiki.jactor2.durable.widgets.string;

import junit.framework.TestCase;
import org.agilewiki.jactor2.common.CFacility;
import org.agilewiki.jactor2.common.CPlant;
import org.agilewiki.jactor2.durable.transactions.DurableReference;
import org.agilewiki.jactor2.durable.transactions.DurableTransaction;
import org.agilewiki.jactor2.common.widgets.WidgetException;

import java.nio.ByteBuffer;

public class StringTest extends TestCase {
    public void test1() throws Exception {
        new CPlant();
        try {
            CFacility facility = (CFacility) CPlant.getInternalFacility();
            StringFactory.addFactorySOp(facility).call();

            StringImpl strImpl = new StringImpl(facility, null, null);
            ByteBuffer buffer = strImpl.createUnmodifiable().duplicateByteBuffer();
            assertEquals(4, buffer.limit());
            assertEquals("", StringImpl.readString(buffer.duplicate()));
            DurableString dStr = strImpl.asWidget();
            assertEquals(0, dStr.length());
            assertEquals("", dStr.getValue());

            dStr.setValue("1");
            assertEquals(1, dStr.length());
            assertEquals("1", dStr.getValue());
            ByteBuffer buffer1 = strImpl.createUnmodifiable().duplicateByteBuffer();
            assertEquals(6, buffer1.limit());
            assertEquals("1", StringImpl.readString(buffer1.duplicate()));

            StringImpl strImpl2 = new StringImpl(facility, null, "-");
            DurableString dstr2 = strImpl2.asWidget();
            assertEquals("-", dstr2.getValue());

            StringImpl strImpl3 = (StringImpl) facility.
                    newInternalWidget(StringFactory.FACTORY_NAME, null, buffer1);
            DurableString dstr3 = strImpl3.asWidget();
            assertEquals("1", dstr3.getValue());

            DurableTransaction setTrans = StringImpl.setValueTransaction("", "42");
            DurableReference durableReference = new DurableReference(strImpl3);
            durableReference.applyAOp(setTrans).call();
            assertEquals("42", dstr3.getValue());
            System.out.println(setTrans.toString());

            DurableTransaction expectTrans = StringImpl.expectTransaction("", "43");
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
