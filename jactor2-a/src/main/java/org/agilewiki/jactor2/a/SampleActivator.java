package org.agilewiki.jactor2.a;

import org.agilewiki.jactor2.common.Activator;
import org.agilewiki.jactor2.common.services.Service;
import org.agilewiki.jactor2.core.messages.AOp;
import org.agilewiki.jactor2.core.messages.AsyncResponseProcessor;
import org.agilewiki.jactor2.core.messages.impl.AsyncRequestImpl;
import org.agilewiki.jactor2.core.reactors.Reactor;
import org.agilewiki.jactor2.core.reactors.facilities.Facility;

public class SampleActivator extends Activator {

    public SampleActivator(Facility _reactor) throws Exception {
        super(_reactor);
    }

    @Override
    public AOp<Void> startAOp() {
        return new AOp<Void>("start", getReactor()) {
            @Override
            protected void processAsyncOperation(final AsyncRequestImpl _asyncRequestImpl,
                                                 final AsyncResponseProcessor<Void> _asyncResponseProcessor)
                    throws Exception {
                SampleService sampleService = new SampleService(getReactor());
                _asyncRequestImpl.syncDirect(sampleService.registerSOp());
                System.out.println("Sample activated!");
                _asyncResponseProcessor.processAsyncResponse(null);
            }
        };
    }
}

class SampleService extends Service {

    protected SampleService(Reactor _reactor) {
        super(_reactor, "sample");
    }
}