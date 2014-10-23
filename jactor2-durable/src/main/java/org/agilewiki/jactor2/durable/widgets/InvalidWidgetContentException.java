package org.agilewiki.jactor2.durable.widgets;

import org.agilewiki.jactor2.common.widgets.WidgetException;

public class InvalidWidgetContentException extends WidgetException {
    public InvalidWidgetContentException(final String _msg) {
        super(_msg);
    }
}
