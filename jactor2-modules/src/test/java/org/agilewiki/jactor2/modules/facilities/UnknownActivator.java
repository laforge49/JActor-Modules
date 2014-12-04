package org.agilewiki.jactor2.modules.facilities;

import org.agilewiki.jactor2.common.Activator;
import org.agilewiki.jactor2.common.services.Service;
import org.agilewiki.jactor2.core.reactors.Facility;
import org.agilewiki.jactor2.core.reactors.NonBlockingReactor;
import org.agilewiki.jactor2.core.requests.AOp;
import org.agilewiki.jactor2.core.requests.AsyncResponseProcessor;
import org.agilewiki.jactor2.core.requests.impl.AsyncRequestImpl;
import org.agilewiki.jactor2.modules.MFacility;
import org.agilewiki.jactor2.modules.MPlant;

public class UnknownActivator extends Activator {
    public UnknownActivator(Facility _reactor) throws Exception {
        super(_reactor);
    }

    @Override
    public AOp<Void> startAOp() {
        return new AOp<Void>("start", getReactor()) {
            @Override
            protected void processAsyncOperation(final AsyncRequestImpl _asyncRequestImpl,
                                                 final AsyncResponseProcessor<Void> _asyncResponseProcessor)
                    throws Exception {
                System.out.println("unknown activated!");
                MFacility a = MPlant.getFacility("A");
                System.out.println(a.getNamedBlades());
                Object u = a.getNamedBlade("sample");
                System.out.println(u);
                Service s = (Service) u;
                System.out.println(s.getClass());
                _asyncResponseProcessor.processAsyncResponse(null);
            }
        };
    }
}
