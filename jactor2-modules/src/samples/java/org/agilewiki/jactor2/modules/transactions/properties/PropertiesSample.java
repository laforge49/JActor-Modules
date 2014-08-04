package org.agilewiki.jactor2.modules.transactions.properties;

import org.agilewiki.jactor2.core.blades.ismTransactions.*;
import org.agilewiki.jactor2.core.blades.pubSub.RequestBus;
import org.agilewiki.jactor2.core.blades.pubSub.SubscribeAOp;
import org.agilewiki.jactor2.core.impl.Plant;
import org.agilewiki.jactor2.core.reactors.CommonReactor;
import org.agilewiki.jactor2.core.reactors.NonBlockingReactor;
import org.agilewiki.jactor2.modules.MPlant;

import java.util.Iterator;
import java.util.SortedMap;

public class PropertiesSample {
    public static void main(final String[] _args) throws Exception {
        new MPlant();
        try {
            ISMReference<String> propertiesReference = new ISMReference<String>(Plant.getInternalFacility());
            final CommonReactor reactor = new NonBlockingReactor();
            RequestBus<ImmutableChanges<String>> validationBus = propertiesReference.validationBus;

            new SubscribeAOp<ImmutableChanges<String>>(
                    validationBus,
                    reactor,
                    new ChangesFilter<String>("immutable.")){
                @Override
                protected void processContent(final ImmutableChanges<String> _content)
                        throws Exception {
                    SortedMap<String, ImmutableChange<String>> readOnlyChanges = _content.readOnlyChanges;
                    final Iterator<ImmutableChange<String>> it = readOnlyChanges.values().iterator();
                    while (it.hasNext()) {
                        final ImmutableChange<String> propertyChange = it.next();
                        if (propertyChange.name.startsWith("immutable.") && propertyChange.oldValue != null) {
                            throw new IllegalArgumentException("Immutable property can not be changed: " +
                                    propertyChange.name);
                        }
                    }
                }
            }.call();

            try {
                new ISMUpdateTransaction<String>("pie", "apple").applyAOp(propertiesReference).call();
                new ISMUpdateTransaction<String>("pie", "peach").applyAOp(propertiesReference).call();
                new ISMUpdateTransaction<String>("pie", (String) null).applyAOp(propertiesReference).call();
                new ISMUpdateTransaction<String>("fruit", "pear").applyAOp(propertiesReference).call();
                new ISMUpdateTransaction<String>("fruit", "orange").applyAOp(propertiesReference).call();
                new ISMUpdateTransaction<String>("immutable.fudge", "fun").applyAOp(propertiesReference).call();
                new ISMUpdateTransaction<String>("immutable.fudge", (String) null).applyAOp(propertiesReference).call(); //raises exception
            } catch (final Exception e) {
                System.out.println(e.getMessage());
            }
            System.out.println(propertiesReference.getImmutable().sortedKeySet());
        } finally {
            Plant.close();
        }
    }
}
