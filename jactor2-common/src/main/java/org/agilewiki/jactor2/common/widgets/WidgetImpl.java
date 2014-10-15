package org.agilewiki.jactor2.common.widgets;

import org.agilewiki.jactor2.core.blades.transmutable.Transmutable;

/**
 * Implements Widget as a nested class.
 */
public abstract class WidgetImpl<DATATYPE> implements Transmutable<DATATYPE> {
    private final WidgetFactory widgetFactory;
    private final _Widget widget;
    private final WidgetImpl parent;

    public WidgetImpl(final WidgetFactory _widgetFactory,
                      final WidgetImpl _parent) {
        widgetFactory = _widgetFactory;
        parent = _parent;
        widget = newWidget();
    }

    public WidgetFactory<DATATYPE> getWidgetFactory() {
        return widgetFactory;
    }

    public WidgetImpl getParent() {
        return parent;
    }

    public _Widget asWidget() {
        return widget;
    }

    protected abstract _Widget newWidget();

    protected class _Widget implements Widget {
    }
}
