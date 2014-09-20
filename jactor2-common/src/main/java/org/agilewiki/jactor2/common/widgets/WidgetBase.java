package org.agilewiki.jactor2.common.widgets;

import org.agilewiki.jactor2.core.blades.BladeBase;
import org.agilewiki.jactor2.core.reactors.Reactor;

public class WidgetBase extends BladeBase implements Widget {
    private final WidgetFactory widgetFactory;
    private final Widget widgetParent;

    public WidgetBase(WidgetFactory _widgetFactory, Widget _widgetParent, Reactor _reactor) {
        widgetFactory = _widgetFactory;
        widgetParent = _widgetParent;
        _initialize(_reactor);
    }

    @Override
    public WidgetFactory getWidgetFactory() {
        return widgetFactory;
    }

    @Override
    public Widget getParentWidget() {
        return widgetParent;
    }
}
