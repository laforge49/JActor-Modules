package org.agilewiki.jactor2.common.widgets;

import org.agilewiki.jactor2.core.blades.Blade;
import org.agilewiki.jactor2.core.blades.NamedBlade;
import org.agilewiki.jactor2.core.reactors.Reactor;

public interface WidgetFactory extends Blade, NamedBlade {
    public Widget newWidget(Reactor _reactor, Widget _parentWidget) throws Exception;
    public String getFactoryKey();
}
