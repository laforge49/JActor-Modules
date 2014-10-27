package org.agilewiki.jactor2.durable.widgets.box;

import junit.framework.TestCase;
import org.agilewiki.jactor2.common.CFacility;
import org.agilewiki.jactor2.common.CPlant;
import org.agilewiki.jactor2.common.widgets.Widget;
import org.agilewiki.jactor2.durable.transactions.DurableReference;
import org.agilewiki.jactor2.durable.transactions.DurableTransaction;
import org.agilewiki.jactor2.durable.widgets.Factories;
import org.agilewiki.jactor2.durable.widgets.UnexpectedValueException;
import org.agilewiki.jactor2.durable.widgets.string.DurableString;
import org.agilewiki.jactor2.durable.widgets.string.StringFactory;

public class BoxTest extends TestCase {
    public void test1() throws Exception {
        new CPlant();
        try {
            CFacility facility = (CFacility) CPlant.getInternalFacility();
            facility.addWidgetFactoriesSOp(new Factories(facility).set).call();
            DurableBox dBox = BoxFactory.newDurableBox(facility);
            String expectedKey = facility.name + ".null";
            assertEquals(expectedKey, dBox.boxedFactoryKey());
            int expectedBufferSize = 4 + 2 * expectedKey.length();
            assertEquals(expectedBufferSize, dBox.getBufferSize());
            assertTrue(dBox.getContent() instanceof Widget);

            DurableReference durableReference = new DurableReference(dBox);
            DurableTransaction expectKeyTrans = BoxFactory.
                    expectedFactoryKeyTransaction(facility, "", expectedKey);
            durableReference.applyAOp(expectKeyTrans).call();

            expectKeyTrans = BoxFactory.
                    expectedFactoryKeyTransaction(facility, "", "ribit");
            Exception ex = null;
            try {
                durableReference.applyAOp(expectKeyTrans).call();
            } catch (UnexpectedValueException uve) {
                ex = uve;
            }
            assertNotNull(ex);

            DurableString dStr = StringFactory.newDurableString(facility, "");
            dBox.putCopy(dStr);
            expectedKey = facility.name + ".str";
            assertEquals(expectedKey, dBox.boxedFactoryKey());
            expectedBufferSize = 4 + 2 * expectedKey.length() + 4;
            assertEquals(expectedBufferSize, dBox.getBufferSize());
            assertTrue(dBox.getContent() instanceof DurableString);
            assertFalse(dStr == dBox.getContent());
            assertEquals(dStr.getValue(), ((DurableString) dBox.getContent()).getValue());

            ((DurableString) dBox.getContent()).setValue("123");
            expectedBufferSize = 4 + 2 * expectedKey.length() + 4 + 2 * 3;
            assertEquals(expectedBufferSize, dBox.getBufferSize());

            DurableTransaction putCopyKeyTrans = BoxFactory.
                    putCopyTransaction(facility, "", dBox);
            durableReference.applyAOp(putCopyKeyTrans).call();

        } finally {
            CPlant.close();
        }
    }
}
