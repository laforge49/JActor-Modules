package org.agilewiki.jactor2.durable.widgets;

import org.agilewiki.jactor2.common.CFacility;
import org.agilewiki.jactor2.common.widgets.WidgetFactory;
import org.agilewiki.jactor2.durable.widgets.box.BoxFactory;
import org.agilewiki.jactor2.durable.widgets.integer.IntFactory;
import org.agilewiki.jactor2.durable.widgets.string.StringFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Factories {
    protected Set<WidgetFactory> pset;
    public final Set<WidgetFactory> set;

    public Factories(final CFacility _facility) {
        set = Collections.unmodifiableSet(makeFactoriesSet(_facility));
    }

    protected Set<WidgetFactory> makeFactoriesSet(final CFacility _facility) {
        pset = new HashSet<>();
        pset.add(new WidgetFactory(_facility));
        pset.add(new IntFactory(_facility));
        pset.add(new StringFactory(_facility));
        pset.add(new BoxFactory(_facility));
        return pset;
    }
}
