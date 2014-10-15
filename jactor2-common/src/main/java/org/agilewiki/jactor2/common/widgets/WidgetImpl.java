package org.agilewiki.jactor2.common.widgets;

import org.agilewiki.jactor2.core.blades.transmutable.Transmutable;

/**
 * Implements Widget as a nested class.
 */
public abstract class WidgetImpl<DATATYPE> implements Transmutable<DATATYPE> {
    private final WidgetFactory widgetFactory;
    private final _Widget widget;
    private final WidgetImpl parent;
    private final DATATYPE unmodifiable;

    public WidgetImpl(final WidgetFactory _widgetFactory,
                      final WidgetImpl _parent,
                      DATATYPE _unmodifiable) {
        widgetFactory = _widgetFactory;
        parent = _parent;
        unmodifiable = _unmodifiable;
        widget = newWidget();
    }

    public WidgetFactory<DATATYPE> getWidgetFactory() {
        return widgetFactory;
    }

    public WidgetImpl getParent() {
        return parent;
    }

    public DATATYPE getUnmodifiable() {
        return unmodifiable;
    }

    public _Widget asWidget() {
        return widget;
    }

    protected _Widget newWidget() {
        return new _Widget();
    }

    protected class _Widget implements Widget {
    }
}
