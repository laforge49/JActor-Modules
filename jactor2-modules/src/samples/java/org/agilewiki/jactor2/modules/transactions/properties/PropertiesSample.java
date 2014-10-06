package org.agilewiki.jactor2.modules.transactions.properties;

import org.agilewiki.jactor2.core.blades.pubSub.RequestBus;
import org.agilewiki.jactor2.core.blades.pubSub.SubscribeAOp;
import org.agilewiki.jactor2.core.blades.transmutable.tssmTransactions.*;
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
            TSSMReference<String> propertiesReference = new TSSMReference<String>(Plant.getInternalFacility());
            final CommonReactor reactor = new NonBlockingReactor();
            RequestBus<TSSMChanges<String>> validationBus = propertiesReference.validationBus;

            new SubscribeAOp<TSSMChanges<String>>(
                    validationBus,
                    reactor,
                    new TSSMPrefixFilter<String>("immutable.")){
                @Override
                protected void processContent(final TSSMChanges<String> _content)
                        throws Exception {
                    SortedMap<String, TSSMChange<String>> readOnlyChanges = _content.unmodifiableChanges;
                    final Iterator<TSSMChange<String>> it = readOnlyChanges.values().iterator();
                    while (it.hasNext()) {
                        final TSSMChange<String> propertyChange = it.next();
                        if (propertyChange.name.startsWith("immutable.") && propertyChange.oldValue != null) {
                            throw new IllegalArgumentException("Immutable property can not be changed: " +
                                    propertyChange.name);
                        }
                    }
                }
            }.call();

            try {
                new TSSMUpdateTransaction<String>("pie", "apple").applyAOp(propertiesReference).call();
                new TSSMUpdateTransaction<String>("pie", "peach").applyAOp(propertiesReference).call();
                new TSSMUpdateTransaction<String>("pie", (String) null).applyAOp(propertiesReference).call();
                new TSSMUpdateTransaction<String>("fruit", "pear").applyAOp(propertiesReference).call();
                new TSSMUpdateTransaction<String>("fruit", "orange").applyAOp(propertiesReference).call();
                new TSSMUpdateTransaction<String>("immutable.fudge", "fun").applyAOp(propertiesReference).call();
                new TSSMUpdateTransaction<String>("immutable.fudge", (String) null).applyAOp(propertiesReference).call(); //raises exception
            } catch (final Exception e) {
                System.out.println(e.getMessage());
            }
            System.out.println(propertiesReference.getUnmodifiable());
        } finally {
            Plant.close();
        }
    }
}
