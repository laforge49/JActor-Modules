package org.agilewiki.jactor2.modules.properties;

import org.agilewiki.jactor2.core.blades.transactions.ISMap;
import org.agilewiki.jactor2.core.blades.transactions.ImmutableReference;
import org.agilewiki.jactor2.core.blades.transactions.SyncTransaction;
import org.agilewiki.jactor2.core.blades.transactions.Transaction;
import org.agilewiki.jactor2.core.requests.AsyncRequest;
import org.agilewiki.jactor2.core.requests.AsyncResponseProcessor;

/**
 * Base class for property transactions.
 */
abstract public class PropertyTransaction extends SyncTransaction<ISMap<String>> implements PropertiesSource {

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

    protected void updateImmutableReference(final ImmutableReference<ISMap<String>> _immutableReference) {
    }

    public AsyncRequest<ISMap<String>> applyAReq(final ImmutableReference<ISMap<String>> _immutableReference) {
        return new AsyncRequest<ISMap<String>>(_immutableReference.getReactor()) {
            private AsyncResponseProcessor<ISMap<String>> dis = this;

            private PropertiesReference propertiesReference = (PropertiesReference) _immutableReference;

            private AsyncResponseProcessor<Void> validationResponseProcessor =
                    new AsyncResponseProcessor<Void>() {
                @Override
                public void processAsyncResponse(Void _response) throws Exception {
                    PropertyTransaction.super.updateImmutableReference(_immutableReference);
                    propertiesChangeManager.close();
                    send(propertiesReference.changeBus.sendsContentAReq(immutablePropertyChanges),
                            dis, immutable);
                }
            };

            private AsyncResponseProcessor<ISMap<String>> superResponseProcessor =
                    new AsyncResponseProcessor<ISMap<String>>() {
                @Override
                public void processAsyncResponse(ISMap<String> _response) throws Exception {
                    immutablePropertyChanges = new ImmutablePropertyChanges(propertiesChangeManager);
                    send(propertiesReference.validationBus.sendsContentAReq(immutablePropertyChanges),
                            validationResponseProcessor);
                }
            };

            @Override
            public void processAsyncRequest() throws Exception {
                propertiesChangeManager = new PropertiesChangeManager(propertiesReference.getImmutable());
                eval(_immutableReference, this, superResponseProcessor);
            }
        };
    }

    @Override
    protected void applySourceReference(final ImmutableReference<ISMap<String>> _propertiesReference) {
        super.applySourceReference(_propertiesReference);
        propertiesChangeManager = new PropertiesChangeManager(_propertiesReference.getImmutable());
    }

    @Override
    protected void applySourceTransaction(final Transaction _transaction) {
        super.applySourceTransaction(_transaction);
        PropertyTransaction propertiesSource = (PropertyTransaction) _transaction;
        propertiesChangeManager = propertiesSource.getPropertiesChangeManager();
    }
}
