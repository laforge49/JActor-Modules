package org.agilewiki.jactor2.common.widgets;

import org.agilewiki.jactor2.core.blades.Blade;
import org.agilewiki.jactor2.core.blades.NamedBlade;
import org.agilewiki.jactor2.core.reactors.Reactor;

public interface WidgetFactory extends Blade, NamedBlade {
    public Widget newWidget(Reactor reactor) throws Exception;
    public Widget newWidget(Reactor reactor, Widget parentWidget) throws Exception;
    public String getFactoryKey();
}
