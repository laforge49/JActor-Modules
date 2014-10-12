package org.agilewiki.jactor2.common.widgets;

import org.agilewiki.jactor2.core.blades.Blade;
import org.agilewiki.jactor2.core.blades.NamedBlade;
import org.agilewiki.jactor2.core.reactors.Reactor;

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
