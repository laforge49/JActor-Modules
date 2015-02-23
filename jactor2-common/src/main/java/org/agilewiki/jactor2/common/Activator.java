package org.agilewiki.jactor2.common;

import org.agilewiki.jactor2.core.blades.IsolationBladeBase;
import org.agilewiki.jactor2.core.messages.AOp;
import org.agilewiki.jactor2.core.reactors.facilities.Facility;

public abstract class Activator extends IsolationBladeBase {
    public Activator(Facility _reactor) throws Exception {
        super(_reactor);
    }

    public abstract AOp<Void> startAOp();
}
