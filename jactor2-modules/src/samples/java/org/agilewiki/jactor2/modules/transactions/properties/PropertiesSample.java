package org.agilewiki.jactor2.modules.transactions.properties;

import org.agilewiki.jactor2.core.impl.Plant;
import org.agilewiki.jactor2.core.reactors.CommonReactor;
import org.agilewiki.jactor2.core.reactors.NonBlockingReactor;
import org.agilewiki.jactor2.modules.MPlant;
import org.agilewiki.jactor2.modules.properties.*;
import org.agilewiki.jactor2.modules.pubSub.RequestBus;
import org.agilewiki.jactor2.modules.pubSub.SubscribeAReq;

import java.util.Iterator;
import java.util.SortedMap;

public class PropertiesSample {
    public static void main(final String[] _args) throws Exception {
        new MPlant();
        try {
            PropertiesReference propertiesReference = new PropertiesReference(Plant.getInternalReactor());
            final CommonReactor reactor = new NonBlockingReactor();
            RequestBus<ImmutablePropertyChanges> validationBus = propertiesReference.validationBus;

            new SubscribeAReq<ImmutablePropertyChanges>(
                    validationBus,
                    reactor,
                    new PropertyChangesFilter("immutable.")){
                @Override
                protected void processContent(final ImmutablePropertyChanges _content)
                        throws Exception {
                    SortedMap<String, PropertyChange> readOnlyChanges = _content.readOnlyChanges;
                    final Iterator<PropertyChange> it = readOnlyChanges.values().iterator();
                    while (it.hasNext()) {
                        final PropertyChange propertyChange = it.next();
                        if (propertyChange.name.startsWith("immutable.") && propertyChange.oldValue != null) {
                            throw new IllegalArgumentException("Immutable property can not be changed: " +
                                    propertyChange.name);
                        }
                    }
                }
            }.call();

            try {
                new UpdatePropertyTransaction("pie", "apple").applyAReq(propertiesReference).call();
                new UpdatePropertyTransaction("pie", "peach").applyAReq(propertiesReference).call();
                new UpdatePropertyTransaction("pie", (String) null).applyAReq(propertiesReference).call();
                new UpdatePropertyTransaction("fruit", "pear").applyAReq(propertiesReference).call();
                new UpdatePropertyTransaction("fruit", "orange").applyAReq(propertiesReference).call();
                new UpdatePropertyTransaction("immutable.fudge", "fun").applyAReq(propertiesReference).call();
                new UpdatePropertyTransaction("immutable.fudge", (String) null).applyAReq(propertiesReference).call(); //raises exception
            } catch (final Exception e) {
                System.out.println(e.getMessage());
            }
            System.out.println(propertiesReference.getImmutable().sortedKeySet());
        } finally {
            Plant.close();
        }
    }
}
