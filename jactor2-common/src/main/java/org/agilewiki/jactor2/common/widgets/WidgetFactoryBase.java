package org.agilewiki.jactor2.common.widgets;

import org.agilewiki.jactor2.common.CFacility;
import org.agilewiki.jactor2.core.blades.BladeBase;
import org.agilewiki.jactor2.core.blades.NamedBlade;

public abstract class WidgetFactoryBase extends BladeBase implements WidgetFactory, NamedBlade {
    public static String factoryKey(String facilityName, String widgetFactoryName) {
        return facilityName + "." + widgetFactoryName;
    }

    public final String name;

    public WidgetFactoryBase(String _name, CFacility _cFacility) {
        name = _name;
        super._initialize(_cFacility);
    }

    @Override
    public String getName() {
        return name;
    }

    public CFacility getCFacility() {
        return (CFacility) getReactor();
    }

    @Override
    public String getFactoryKey() {
        return factoryKey(getCFacility().name, name);
    }
}
