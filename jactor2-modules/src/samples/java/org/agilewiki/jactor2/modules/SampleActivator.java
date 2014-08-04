package org.agilewiki.jactor2.modules;


import org.agilewiki.jactor2.core.blades.BladeBase;
import org.agilewiki.jactor2.core.reactors.NonBlockingReactor;
import org.agilewiki.jactor2.core.requests.AOp;
import org.agilewiki.jactor2.core.requests.AsyncResponseProcessor;
import org.agilewiki.jactor2.core.requests.impl.AsyncRequestImpl;

public class SampleActivator extends Activator {

    public SampleActivator(NonBlockingReactor _reactor) throws Exception {
        super(_reactor);
    }

    @Override
    public AOp<Void> startAOp() {
        return new AOp<Void>("start", getReactor()) {
            @Override
            public void processAsyncOperation(final AsyncRequestImpl _asyncRequestImpl,
                                              final AsyncResponseProcessor<Void> _asyncResponseProcessor)
                    throws Exception {
                System.out.println("activated!");
                _asyncResponseProcessor.processAsyncResponse(null);
            }
        };
    }
}
