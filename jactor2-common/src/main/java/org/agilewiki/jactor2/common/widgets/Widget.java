package org.agilewiki.jactor2.common.widgets;

import org.agilewiki.jactor2.core.blades.Blade;

public interface Widget {
    public WidgetFactory getWidgetFactory();
    public Widget getParentWidget();
}
