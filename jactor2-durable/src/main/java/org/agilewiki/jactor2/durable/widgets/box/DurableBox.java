package org.agilewiki.jactor2.durable.widgets.box;

import org.agilewiki.jactor2.common.widgets.InternalWidget;
import org.agilewiki.jactor2.durable.widgets.UnexpectedValueException;

public interface DurableBox {
    String boxedFactoryKey();
    void expectedFactoryKey(String _expected) throws UnexpectedValueException;
    InternalWidget boxedInternalWidget();
    void empty();
    boolean isEmpty();
    void putCopy(InternalWidget _internalWidget);
}
