package org.agilewiki.jactor2.common;

import junit.framework.TestCase;
import org.agilewiki.jactor2.core.blades.NamedBlade;
import org.agilewiki.jactor2.core.blades.pubSub.SubscribeAOp;
import org.agilewiki.jactor2.core.impl.Plant;
import org.agilewiki.jactor2.core.reactors.facilities.RegistrationNotification;

public class RegistrationTest extends TestCase {
    public void test1() throws Exception {
        new Plant();
        try {
            final NamedBlade namedBlade = new NamedBlade() {
                @Override
                public String getName() {
                    return "FooBar";
                }
            };
            final CFacility facility = new CFacility("TestFacility");
            new SubscribeAOp<RegistrationNotification>(
                    facility.registrationNotifier, facility) {
                @Override
                protected void processContent(
                        final RegistrationNotification registrationNotification) {
                    if (registrationNotification.isRegistration())
                        System.out.println("registered: "
                                + registrationNotification.name);
                    else
                        System.out.println("unregistered: "
                                + registrationNotification.name);
                }
            }.call();
            facility.registerBladeSOp(namedBlade).call();
            facility.unregisterBladeSOp("TestFacility").call();
            facility.unregisterBladeSOp("FooBar").call();
        } finally {
            Plant.close();
        }
    }
}
