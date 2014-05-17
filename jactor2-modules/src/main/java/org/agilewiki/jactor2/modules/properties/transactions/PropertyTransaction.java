package org.agilewiki.jactor2.modules.properties.transactions;

import org.agilewiki.jactor2.core.blades.transactions.ImmutableReference;
import org.agilewiki.jactor2.core.blades.transactions.SyncTransaction;
import org.agilewiki.jactor2.core.blades.transactions.Transaction;
import org.agilewiki.jactor2.core.requests.AsyncRequest;
import org.agilewiki.jactor2.core.requests.AsyncResponseProcessor;
import org.agilewiki.jactor2.modules.properties.immutable.ImmutableProperties;

/**
 * Base class for property transactions.
 */
abstract public class PropertyTransaction extends SyncTransaction<ImmutableProperties> implements PropertiesSource {

    protected PropertiesChangeManager propertiesChangeManager;

    private ImmutablePropertyChanges immutablePropertyChanges;

    /**
     * Create a PropertyTransaction.
     */
    public PropertyTransaction() {
        super(null);
    }

    /**
     * Compose a PropertyTransaction.
     *
     * @param _parent The PropertyTransaction to be applied before this one.
     */
    public PropertyTransaction(final PropertyTransaction _parent) {
        super(_parent);
    }

    public PropertiesChangeManager getPropertiesChangeManager() {
        return propertiesChangeManager;
    }

    protected void updateImmutableReference(final ImmutableReference<ImmutableProperties> _immutableReference) {
    }

    public AsyncRequest<ImmutableProperties> applyAReq(final ImmutableReference<ImmutableProperties> _immutableReference) {
        return new AsyncRequest<ImmutableProperties>(_immutableReference.getReactor()) {
            private AsyncResponseProcessor<ImmutableProperties> dis = this;

            private PropertiesReference propertiesReference = (PropertiesReference) _immutableReference;

            private AsyncResponseProcessor<Void> validationResponseProcessor =
                    new AsyncResponseProcessor<Void>() {
                @Override
                public void processAsyncResponse(Void _response) throws Exception {
                    PropertyTransaction.super.updateImmutableReference(_immutableReference);
                    send(propertiesReference.changeBus.sendsContentAReq(immutablePropertyChanges),
                            dis, immutable);
                }
            };

            private AsyncResponseProcessor<ImmutableProperties> superResponseProcessor =
                    new AsyncResponseProcessor<ImmutableProperties>() {
                @Override
                public void processAsyncResponse(ImmutableProperties _response) throws Exception {
                    immutablePropertyChanges = new ImmutablePropertyChanges(propertiesChangeManager);
                    send(propertiesReference.validationBus.sendsContentAReq(immutablePropertyChanges),
                            validationResponseProcessor);
                }
            };

            @Override
            public void processAsyncRequest() throws Exception {
                send(PropertyTransaction.super.applyAReq(_immutableReference), superResponseProcessor);
            }
        };
    }
}
