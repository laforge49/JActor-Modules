package org.agilewiki.jactor2.modules.properties;

import junit.framework.TestCase;
import org.agilewiki.jactor2.core.impl.Plant;
import org.agilewiki.jactor2.core.reactors.CommonReactor;
import org.agilewiki.jactor2.core.reactors.NonBlockingReactor;
import org.agilewiki.jactor2.modules.MPlant;
import org.agilewiki.jactor2.modules.properties.immutable.ImmutableProperties;
import org.agilewiki.jactor2.modules.properties.transactions.ImmutablePropertyChanges;
import org.agilewiki.jactor2.modules.properties.transactions.PropertiesReference;
import org.agilewiki.jactor2.modules.properties.transactions.PropertyChange;
import org.agilewiki.jactor2.modules.properties.transactions.UpdatePropertyTransaction;
import org.agilewiki.jactor2.modules.pubSub.RequestBus;
import org.agilewiki.jactor2.modules.pubSub.SubscribeAReq;

import java.io.IOException;
import java.util.Iterator;
import java.util.SortedMap;

public class PTest extends TestCase {
    public void testI() throws Exception {
        new MPlant();
        try {
            PropertiesReference propertiesReference = new PropertiesReference();
            final CommonReactor reactor = new NonBlockingReactor();

            RequestBus<ImmutablePropertyChanges> validationBus = propertiesReference.validationBus;
            new SubscribeAReq<ImmutablePropertyChanges>(validationBus, reactor){
                @Override
                protected void processContent(final ImmutablePropertyChanges _content)
                        throws Exception {
                    SortedMap<String, PropertyChange> readOnlyChanges = _content.readOnlyChanges;
                    final Iterator<PropertyChange> it = readOnlyChanges.values().iterator();
                    while (it.hasNext()) {
                        final PropertyChange propertyChange = it.next();
                        if (propertyChange.name.equals("fudge")) {
                            throw new IOException("no way");
                        }
                    }
                }
            }.call();

            RequestBus<ImmutablePropertyChanges> changeBus = propertiesReference.changeBus;
            new SubscribeAReq<ImmutablePropertyChanges>(changeBus, reactor){
                @Override
                protected void processContent(final ImmutablePropertyChanges _content)
                        throws Exception {
                    SortedMap<String, PropertyChange> readOnlyChanges = _content.readOnlyChanges;
                    System.out.println("\nchanges: " + readOnlyChanges.size());
                    final Iterator<PropertyChange> it = readOnlyChanges.values().iterator();
                    while (it.hasNext()) {
                        final PropertyChange propertyChange = it.next();
                        System.out.println("key=" + propertyChange.name + " old="
                                + propertyChange.oldValue + " new="
                                + propertyChange.newValue);
                    }
                }
            }.call();

            ImmutableProperties immutableState = propertiesReference.getImmutable();
            assertEquals(0, immutableState.size());

            new UpdatePropertyTransaction("1", "first").applyAReq(propertiesReference).call();
            assertEquals(0, immutableState.size());
            immutableState = propertiesReference.getImmutable();
            assertEquals(1, immutableState.size());

            new UpdatePropertyTransaction("1", "second").applyAReq(propertiesReference).call();
            assertEquals(1, immutableState.size());
            immutableState = propertiesReference.getImmutable();
            assertEquals(1, immutableState.size());

            String msg = null;
            try {
                new UpdatePropertyTransaction("fudge", "second").applyAReq(propertiesReference).call();
            } catch (final Exception e) {
                msg = e.getMessage();
            }
            assertEquals("no way", msg);
            assertEquals(1, immutableState.size());
            immutableState = propertiesReference.getImmutable();
            assertEquals(1, immutableState.size());

            new UpdatePropertyTransaction("1", (String) null).applyAReq(propertiesReference).call();
            immutableState = propertiesReference.getImmutable();
            assertEquals(0, immutableState.size());
        } finally {
            Plant.close();
        }
    }
}
