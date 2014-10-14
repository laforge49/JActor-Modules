package org.agilewiki.jactor2.common.widgets;

/**
 * Implements Widget as a nested class.
 */
public class WidgetImpl {
    private final WidgetFactory widgetFactory;
    private final Widget widget;
    private final WidgetImpl parent;

    public WidgetImpl(final WidgetFactory _widgetFactory, final WidgetImpl _parent) {
        widgetFactory = _widgetFactory;
        parent = _parent;
        widget = new Widget() {
            @Override
            public WidgetFactory getWidgetFactory() {
                return widgetFactory;
            }
        };
    }

    public WidgetFactory getWidgetFactory() {
        return widgetFactory;
    }

    public WidgetImpl getParentWidget() {
        return parent;
    }

    public Widget asWidget() {
        return widget;
    }
}
