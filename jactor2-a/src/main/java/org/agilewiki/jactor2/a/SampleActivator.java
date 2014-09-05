package org.agilewiki.jactor2.a;

import org.agilewiki.jactor2.common.service.Service;
import org.agilewiki.jactor2.core.reactors.NonBlockingReactor;
import org.agilewiki.jactor2.core.reactors.Reactor;
import org.agilewiki.jactor2.core.requests.AOp;
import org.agilewiki.jactor2.core.requests.AsyncResponseProcessor;
import org.agilewiki.jactor2.core.requests.impl.AsyncRequestImpl;
import org.agilewiki.jactor2.common.Activator;

public class SampleActivator extends Activator {

    public SampleActivator(NonBlockingReactor _reactor) throws Exception {
        super(_reactor);
    }

    @Override
    public AOp<Void> startAOp() {
        return new AOp<Void>("start", getReactor()) {
            @Override
            protected void processAsyncOperation(final AsyncRequestImpl _asyncRequestImpl,
                                            final AsyncResponseProcessor<Void> _asyncResponseProcessor)
                    throws Exception {
                SimpleService simple = new SimpleService(getReactor());
                _asyncRequestImpl.syncDirect(simple.registerSOp());
                System.out.println("activated!");
                _asyncResponseProcessor.processAsyncResponse(null);
            }
        };
    }
}

class SimpleService extends Service {

    protected SimpleService(Reactor _reactor) {
        super(_reactor, "simple");
    }
}