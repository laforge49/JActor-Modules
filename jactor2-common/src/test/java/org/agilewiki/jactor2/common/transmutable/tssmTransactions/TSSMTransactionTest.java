package org.agilewiki.jactor2.common.transmutable.tssmTransactions;

import junit.framework.TestCase;
import org.agilewiki.jactor2.common.pubSub.RequestBus;
import org.agilewiki.jactor2.common.pubSub.SubscribeAOp;
import org.agilewiki.jactor2.core.impl.Plant;
import org.agilewiki.jactor2.core.reactors.IsolationReactor;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;

public class TSSMTransactionTest extends TestCase {
    public void testI() throws Exception {
        new Plant();
        try {
            final TSSMReference<String> propertiesReference = new TSSMReference<String>();
            final IsolationReactor reactor = new IsolationReactor();

            final RequestBus<TSSMChanges<String>> validationBus = propertiesReference.validationBus;
            new SubscribeAOp<TSSMChanges<String>>(validationBus,
                    reactor) {
                @Override
                protected void processContent(
                        final TSSMChanges<String> _content)
                        throws Exception {
                    final List<TSSMChange<String>> readOnlyChanges = _content.unmodifiableChanges;
                    final Iterator<TSSMChange<String>> it = readOnlyChanges.iterator();
                    while (it.hasNext()) {
                        final TSSMChange<String> propertyChange = it
                                .next();
                        if (propertyChange.name.equals("fudge")) {
                            throw new IOException("no way");
                        }
                    }
                }
            }.call();

            final RequestBus<TSSMChanges<String>> changeBus = propertiesReference.changeBus;
            new SubscribeAOp<TSSMChanges<String>>(changeBus, reactor) {
                @Override
                protected void processContent(
                        final TSSMChanges<String> _content)
                        throws Exception {
                    final List<TSSMChange<String>> readOnlyChanges = _content.unmodifiableChanges;
                    System.out.println("\nchanges: " + readOnlyChanges.size());
                    final Iterator<TSSMChange<String>> it = readOnlyChanges.iterator();
                    while (it.hasNext()) {
                        final TSSMChange<String> propertyChange = it
                                .next();
                        System.out.println("key=" + propertyChange.name
                                + " old=" + propertyChange.oldValue + " new="
                                + propertyChange.newValue);
                    }
                }
            }.call();

            SortedMap<String, String> immutableState = propertiesReference.getUnmodifiable();
            assertEquals(0, immutableState.size());

            propertiesReference.applyAOp(new TSSMUpdateTransaction<String>("1", "first")).call();
            assertEquals(0, immutableState.size());
            immutableState = propertiesReference.getUnmodifiable();
            assertEquals(1, immutableState.size());

            propertiesReference.applyAOp(new TSSMUpdateTransaction<String>("1", "second")).call();
            assertEquals(1, immutableState.size());
            immutableState = propertiesReference.getUnmodifiable();
            assertEquals(1, immutableState.size());

            String msg = null;
            try {
                propertiesReference.applyAOp(new TSSMUpdateTransaction<String>("fudge", "second")).call();
            } catch (final IOException e) {
                msg = e.getMessage();
            }

            assertEquals("no way", msg);
            assertEquals(1, immutableState.size());
            immutableState = propertiesReference.getUnmodifiable();
            assertEquals(1, immutableState.size());

            TSSMUpdateTransaction ut = new TSSMUpdateTransaction<String>("1", (String) null);
            propertiesReference.applyAOp(ut).call();
            System.out.println(ut);
            immutableState = propertiesReference.getUnmodifiable();
            assertEquals(0, immutableState.size());
        } finally {
            Plant.close();
        }
    }
}
