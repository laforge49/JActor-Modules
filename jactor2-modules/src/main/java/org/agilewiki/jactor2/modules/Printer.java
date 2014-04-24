package org.agilewiki.jactor2.modules;

import org.agilewiki.jactor2.core.blades.BlockingBladeBase;
import org.agilewiki.jactor2.core.impl.Plant;
import org.agilewiki.jactor2.core.reactors.BlockingReactor;
import org.agilewiki.jactor2.core.reactors.Reactor;
import org.agilewiki.jactor2.core.requests.AsyncRequest;
import org.agilewiki.jactor2.core.requests.AsyncResponseProcessor;
import org.agilewiki.jactor2.core.requests.SyncRequest;

/**
 * <p>
 * A blocking blade is used for printing.
 * </p>
 * <h3>Sample Usage:</h3>
 * <pre>
 * public class PrinterSample {
 *
 *     public static void main(String[] args) throws Exception {
 *
 *         //A facility with one thread.
 *         final Plant plant = new Plant(1);
 *
 *         try {
 *
 *             //Print something.
 *             Printer.printlnAReq(plant, "Hello World!").call();
 *
 *         } finally {
 *             //shutdown the plant
 *             plant.close();
 *         }
 *
 *     }
 * }
 * </pre>
 */
public class Printer extends BlockingBladeBase {

    private static Printer printer;

    public static AsyncRequest<Void> printlnAReq(final String _string) {
        return new AsyncRequest<Void>(Plant.getInternalReactor()) {
            AsyncResponseProcessor<Void> dis = this;

            @Override
            public void processAsyncRequest() {
                send(stdoutAReq(),
                        new AsyncResponseProcessor<Printer>() {
                            @Override
                            public void processAsyncResponse(
                                    final Printer _printer) {
                                send(_printer.printlnSReq(_string), dis);
                            }
                        });
            }
        };
    }

    public static AsyncRequest<Void> printfAReq(final String _format, final Object... _args) {
        return new AsyncRequest<Void>(Plant.getInternalReactor()) {
            AsyncResponseProcessor<Void> dis = this;

            @Override
            public void processAsyncRequest() {
                send(stdoutAReq(),
                        new AsyncResponseProcessor<Printer>() {
                            @Override
                            public void processAsyncResponse(
                                    final Printer _printer) {
                                send(_printer.printfSReq(_format, _args), dis);
                            }
                        });
            }
        };
    }

    static public AsyncRequest<Printer> stdoutAReq() {
        return new AsyncRequest<Printer>(Plant.getInternalReactor()) {
            AsyncResponseProcessor<Printer> dis = this;

            @Override
            public void processAsyncRequest() throws Exception {
                if (printer != null) {
                    dis.processAsyncResponse(printer);
                    return;
                }
                printer = new Printer();
                dis.processAsyncResponse(printer);
            }
        };
    }

    /**
     * Create a Printer blades.
     */
    private Printer() throws Exception {
        super(new BlockingReactor());
    }

    public void println(final Reactor _sourceReactor, final String _string) {
        directCheck(_sourceReactor);
        System.out.println(_string);
    }

    /**
     * A request to print a string.
     *
     * @param _string The string to be printed
     * @return The request.
     */
    public SyncRequest<Void> printlnSReq(final String _string) {
        return new SyncBladeRequest<Void>() {
            @Override
            public Void processSyncRequest() throws Exception {
                System.out.println(_string);
                return null;
            }
        };
    }

    public void printf(final Reactor _sourceReactor, final String _format,
                       final Object... _args) {
        directCheck(_sourceReactor);
        System.out.print(String.format(_format, _args));
    }

    /**
     * A request to print a formated string.
     *
     * @param _format The formatting.
     * @param _args   The data to be formatted.
     * @return The request.
     */
    public SyncRequest<Void> printfSReq(final String _format,
            final Object... _args) {
        return new SyncBladeRequest<Void>() {
            @Override
            public Void processSyncRequest() throws Exception {
                System.out.print(String.format(_format, _args));
                return null;
            }
        };
    }
}
