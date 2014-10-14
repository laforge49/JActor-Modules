package org.agilewiki.jactor2.common;

import junit.framework.TestCase;
import org.agilewiki.jactor2.common.widgets.WidgetFactoryBase;
import org.agilewiki.jactor2.common.widgets.WidgetImpl;

public class WidgetTest extends TestCase {
    public void test1() throws Exception {
        new CPlant();
        try {
            CFacility facility = (CFacility) CPlant.getInternalFacility();
            facility.addWidgetFactorySOp(new WidgetFactoryBase("test", facility)).call();
            WidgetImpl widgetImpl = facility.newWidgetImpl("test");
            System.out.println(widgetImpl);
        } finally {
            CPlant.close();
        }
    }
}
