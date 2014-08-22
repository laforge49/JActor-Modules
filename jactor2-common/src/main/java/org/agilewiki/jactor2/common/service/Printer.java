package org.agilewiki.jactor2.common.service;

import org.agilewiki.jactor2.common.CPlant;
import org.agilewiki.jactor2.common.CPlantImpl;
import org.agilewiki.jactor2.common.Service;
import org.agilewiki.jactor2.core.reactors.Facility;
import org.agilewiki.jactor2.core.reactors.Reactor;
import org.agilewiki.jactor2.core.requests.SOp;

public abstract class Printer extends Service {
    public static Printer getPrinter() throws Exception {
        return getPrinter(CPlantImpl.getSingleton().getInternalFacility());
    }

    public static Printer getPrinter(final Reactor _reactor) throws Exception {
        Facility facility = CPlant.getFacility(_reactor);
        return (Printer) facility.getBlade("printer");
    }

    protected Printer(final Reactor _reactor) {
        super(_reactor, "printer");
    }

    /**
     * A request to print a string.
     *
     * @param _string The string to be printed
     * @return The request.
     */
    public abstract SOp<Void> printlnSOp(final String _string);

    /**
     * A request to print a formated string.
     *
     * @param _format The formatting.
     * @param _args   The data to be formatted.
     * @return The request.
     */
    public abstract SOp<Void> printfSOp(final String _format,
                               final Object... _args);
}
