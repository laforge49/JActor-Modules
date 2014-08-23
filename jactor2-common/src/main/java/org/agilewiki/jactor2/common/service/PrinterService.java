package org.agilewiki.jactor2.common.service;

import org.agilewiki.jactor2.common.CPlant;
import org.agilewiki.jactor2.common.CPlantImpl;
import org.agilewiki.jactor2.common.Service;
import org.agilewiki.jactor2.core.reactors.BlockingReactor;
import org.agilewiki.jactor2.core.reactors.Facility;
import org.agilewiki.jactor2.core.reactors.Reactor;
import org.agilewiki.jactor2.core.requests.SOp;
import org.agilewiki.jactor2.core.requests.impl.RequestImpl;

public class PrinterService extends Service {
    public static PrinterService getPrinter() throws Exception {
        return getPrinter(CPlantImpl.getSingleton().getInternalFacility());
    }

    public static PrinterService getPrinter(final Reactor _reactor) throws Exception {
        Facility facility = CPlant.getFacility(_reactor);
        return (PrinterService) facility.getBlade("printer");
    }

    public static void register() throws Exception {
        new PrinterService().registerSOp().call();
    }

    public PrinterService() throws Exception {
        this(new BlockingReactor());
    }

    public PrinterService(final Reactor _reactor) {
        super(_reactor, "printer");
    }

    /**
     * A request to print a string.
     *
     * @param _string The string to be printed
     * @return The request.
     */
    public SOp<Void> printlnSOp(final String _string) {
        return new SOp<Void>("printlnS", getReactor()) {
            @Override
            protected Void processSyncOperation(final RequestImpl _requestImpl) throws Exception {
                System.out.println(_string);
                return null;
            }
        };
    }

    /**
     * A request to print a formated string.
     *
     * @param _format The formatting.
     * @param _args   The data to be formatted.
     * @return The request.
     */
    public SOp<Void> printfSOp(final String _format, final Object... _args) {
        return new SOp<Void>("printfS", getReactor()) {
            @Override
            protected Void processSyncOperation(final RequestImpl _requestImpl) throws Exception {
                System.out.print(String.format(_format, _args));
                return null;
            }
        };
    }
}
