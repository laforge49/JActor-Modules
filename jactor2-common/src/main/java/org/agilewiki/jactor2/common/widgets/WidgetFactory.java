package org.agilewiki.jactor2.common.widgets;

public interface WidgetFactory {
    public Widget newWidget(Widget _parentWidget) throws Exception;
    public String getFactoryKey();

    /**
     * Returns the blade's name.
     *
     * @return The name of the blade, or null.
     */
    String getName();
}
