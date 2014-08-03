package org.agilewiki.jactor2.modules;

import org.agilewiki.jactor2.core.blades.NonBlockingBladeBase;
import org.agilewiki.jactor2.core.reactors.NonBlockingReactor;
import org.agilewiki.jactor2.core.requests.AOp;

public abstract class Activator extends NonBlockingBladeBase {
    public Activator(NonBlockingReactor _reactor) throws Exception {
        super(_reactor);
    }

    public abstract AOp<Void> startAOp();
}
