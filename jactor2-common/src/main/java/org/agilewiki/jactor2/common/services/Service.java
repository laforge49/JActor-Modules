package org.agilewiki.jactor2.common.services;

import org.agilewiki.jactor2.common.CPlant;
import org.agilewiki.jactor2.core.blades.BladeBase;
import org.agilewiki.jactor2.core.blades.NamedBlade;
import org.agilewiki.jactor2.core.messages.SOp;
import org.agilewiki.jactor2.core.reactors.Reactor;
import org.agilewiki.jactor2.core.reactors.facilities.Facility;

public abstract class Service extends BladeBase implements NamedBlade {
    private final String name;

    protected Service(final Reactor _reactor, final String _name) {
        _initialize(_reactor);
        name = _name;
    }

    @Override
    public String getName() {
        return name;
    }

    public SOp<Void> registerSOp() {
        Facility facility = CPlant.getFacility(getReactor());
        return facility.registerBladeSOp(this);
    }

    public SOp<NamedBlade> unregisterSOp() {
        Facility facility = CPlant.getFacility(getReactor());
        return facility.unregisterBladeSOp(getName());
    }
}
