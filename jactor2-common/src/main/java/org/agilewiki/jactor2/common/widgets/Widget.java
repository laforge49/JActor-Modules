package org.agilewiki.jactor2.common.widgets;

import org.agilewiki.jactor2.core.blades.transmutable.Transmutable;

public interface Widget<DATATYPE> extends Transmutable<DATATYPE> {
    public WidgetFactory getWidgetFactory();
}
