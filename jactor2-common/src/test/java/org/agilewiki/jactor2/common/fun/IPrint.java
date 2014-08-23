package org.agilewiki.jactor2.common.fun;

import org.agilewiki.jactor2.core.requests.impl.RequestImpl;

public class IPrint extends IntBlade.IntBladeSOp<Void> {
    public IPrint(IntBlade _intBlade) {
        super("iprint", _intBlade);
    }

    @Override
    protected Void processSyncOperation(RequestImpl _requestImpl) {
        System.out.println(getInt());
        return null;
    }
}
