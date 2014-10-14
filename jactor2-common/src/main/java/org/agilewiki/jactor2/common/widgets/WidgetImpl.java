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
        widget = newWidget();
    }

    public WidgetFactory getWidgetFactory() {
        return widgetFactory;
    }

    public WidgetImpl getParent() {
        return parent;
    }

    public Widget asWidget() {
        return widget;
    }

    protected Widget newWidget() {
        return new Widget() {
            @Override
            public WidgetFactory getWidgetFactory() {
                return widgetFactory;
            }
        };
    }
}
