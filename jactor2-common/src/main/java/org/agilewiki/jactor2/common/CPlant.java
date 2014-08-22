package org.agilewiki.jactor2.common;

import org.agilewiki.jactor2.core.impl.mtPlant.PlantConfiguration;
import org.agilewiki.jactor2.core.plant.PlantBase;
import org.agilewiki.jactor2.core.reactors.Facility;
import org.agilewiki.jactor2.core.reactors.Reactor;

public class CPlant extends PlantBase {
    public static Facility getFacility(Reactor _reactor) {
        while (!(_reactor instanceof Facility)) _reactor = _reactor.getParentReactor();
        return (Facility) _reactor;
    }

    /**
     * Create a plant with the default configuration.
     */
    public CPlant() throws Exception {
        new CPlantImpl();
    }

    /**
     * Create a plant with the default configuration,
     * but with the given reactor thread pool size.
     *
     * @param _reactorThreadPoolSize The number of threads to be created for the
     *                               reactor thread pool.
     */
    public CPlant(final int _reactorThreadPoolSize) throws Exception {
        new CPlantImpl(_reactorThreadPoolSize);
    }

    /**
     * Create a plant with the given configuration.
     *
     * @param _plantConfiguration The configuration to be used by the plant.
     */
    public CPlant(final PlantConfiguration _plantConfiguration) throws Exception {
        new CPlantImpl(_plantConfiguration);
    }
}
