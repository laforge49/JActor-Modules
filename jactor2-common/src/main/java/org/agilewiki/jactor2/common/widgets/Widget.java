package org.agilewiki.jactor2.common.widgets;

public interface Widget {
    public Widget resolve(String _path) throws InvalidWidgetPathException;
}
