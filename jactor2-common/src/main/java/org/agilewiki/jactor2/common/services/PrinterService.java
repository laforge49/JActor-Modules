package org.agilewiki.jactor2.common.services;

import org.agilewiki.jactor2.common.CFacility;
import org.agilewiki.jactor2.common.CPlant;
import org.agilewiki.jactor2.common.CPlantImpl;
import org.agilewiki.jactor2.core.messages.SOp;
import org.agilewiki.jactor2.core.messages.impl.RequestImpl;
import org.agilewiki.jactor2.core.reactors.BlockingReactor;
import org.agilewiki.jactor2.core.reactors.Reactor;

public class PrinterService extends Service {
    public static final String PRINTER_SERVICE_NAME = "printer";

    public static PrinterService getPrinterService() throws Exception {
        return getPrinterService(CPlantImpl.getSingleton().getInternalFacility());
    }

    public static PrinterService getPrinterService(final Reactor _reactor) throws Exception {
        CFacility facility = CPlant.getFacility(_reactor);
        return (PrinterService) facility.getNamedBlade(PRINTER_SERVICE_NAME);
    }

    public static void register() throws Exception {
        new PrinterService().registerSOp().call();
    }

    public PrinterService() throws Exception {
        this(new BlockingReactor());
    }

    public PrinterService(final BlockingReactor _reactor) {
        super(_reactor, PRINTER_SERVICE_NAME);
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
