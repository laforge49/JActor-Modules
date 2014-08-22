package org.agilewiki.jactor2.common;

import org.agilewiki.jactor2.core.impl.mtPlant.PlantConfiguration;
import org.agilewiki.jactor2.core.impl.mtPlant.PlantMtImpl;

public class CPlantImpl extends PlantMtImpl {

    public CPlantImpl() throws Exception {
        this(new PlantConfiguration());
    }

    public CPlantImpl(final int _threadCount) throws Exception {
        this(new PlantConfiguration(_threadCount));
    }

    public CPlantImpl(final PlantConfiguration _plantConfiguration) throws Exception {
        super(_plantConfiguration);
    }
}
