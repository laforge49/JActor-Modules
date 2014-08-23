package org.agilewiki.jactor2.common.fun;

import org.agilewiki.jactor2.core.requests.impl.RequestImpl;

public class ISet extends IntBlade.IntBladeSOp<Void> {
    protected final int i;

    public ISet(IntBlade _intBlade, int _i) {
        super("iset", _intBlade);
        i = _i;
    }

    @Override
    protected Void processSyncOperation(RequestImpl _requestImpl) {
        setInt(i);
        return null;
    }
}
