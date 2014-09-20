package org.agilewiki.jactor2.common;

import junit.framework.TestCase;
import org.agilewiki.jactor2.common.widgets.Widget;
import org.agilewiki.jactor2.common.widgets.WidgetFactoryBase;

public class WidgetTest extends TestCase {
    public void test1() throws Exception {
        new CPlant();
        try {
            CFacility facility = (CFacility) CPlant.getInternalFacility();
            facility.addWidgetFactorySOp(new WidgetFactoryBase("test", facility)).call();
            Widget widget = facility.newWidget("test", facility);
            System.out.println(widget);
        } finally {
            CPlant.close();
        }
    }
}
