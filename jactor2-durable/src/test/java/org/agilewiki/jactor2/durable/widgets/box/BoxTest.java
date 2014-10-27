package org.agilewiki.jactor2.durable.widgets.box;

import junit.framework.TestCase;
import org.agilewiki.jactor2.common.CFacility;
import org.agilewiki.jactor2.common.CPlant;
import org.agilewiki.jactor2.durable.widgets.Factories;

public class BoxTest extends TestCase {
    public void test1() throws Exception {
        new CPlant();
        try {
            CFacility facility = (CFacility) CPlant.getInternalFacility();
            facility.addWidgetFactoriesSOp(new Factories(facility).set).call();
        } finally {
            CPlant.close();
        }
    }
}
