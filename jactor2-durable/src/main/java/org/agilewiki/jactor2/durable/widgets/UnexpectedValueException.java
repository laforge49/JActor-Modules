package org.agilewiki.jactor2.durable.widgets;

import org.agilewiki.jactor2.common.widgets.WidgetException;

public class UnexpectedValueException extends WidgetException {
    public UnexpectedValueException(final String _msg) {
        super(_msg);
    }
}
