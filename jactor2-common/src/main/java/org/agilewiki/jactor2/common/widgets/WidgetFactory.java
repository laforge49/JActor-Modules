package org.agilewiki.jactor2.common.widgets;

public interface WidgetFactory {
    public WidgetImpl newWidgetImpl(WidgetImpl _parent) throws Exception;

    public String getFactoryKey();

    /**
     * Returns the blade's name.
     *
     * @return The name of the blade, or null.
     */
    String getName();
}
