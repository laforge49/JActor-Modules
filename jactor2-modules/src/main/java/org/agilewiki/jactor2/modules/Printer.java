package org.agilewiki.jactor2.modules;

import org.agilewiki.jactor2.core.blades.BlockingBladeBase;
import org.agilewiki.jactor2.core.impl.Plant;
import org.agilewiki.jactor2.core.reactors.BlockingReactor;
import org.agilewiki.jactor2.core.reactors.Reactor;
import org.agilewiki.jactor2.core.requests.AOp;
import org.agilewiki.jactor2.core.requests.AsyncResponseProcessor;
import org.agilewiki.jactor2.core.requests.SOp;
import org.agilewiki.jactor2.core.requests.impl.AsyncRequestImpl;
import org.agilewiki.jactor2.core.requests.impl.RequestImpl;

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
 *             Printer.printlnAOp(plant, "Hello World!").call();
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

    public static AOp<Void> printlnAOp(final String _string) {
        return new AOp<Void>("printlnA", Plant.getInternalFacility()) {
            @Override
            public void processAsyncOperation(final AsyncRequestImpl _asyncRequestImpl,
                                              final AsyncResponseProcessor<Void> _asyncResponseProcessor) {
                _asyncRequestImpl.send(stdoutAOp(),
                        new AsyncResponseProcessor<Printer>() {
                            @Override
                            public void processAsyncResponse(
                                    final Printer _printer) {
                                _asyncRequestImpl.send(_printer.printlnSOp(_string), _asyncResponseProcessor);
                            }
                        });
            }
        };
    }

    public static AOp<Void> printfAOp(final String _format, final Object... _args) {
        return new AOp<Void>("printfA", Plant.getInternalFacility()) {
            @Override
            public void processAsyncOperation(final AsyncRequestImpl _asyncRequestImpl,
                                            final AsyncResponseProcessor<Void> _asyncResponseProcessor) {
                _asyncRequestImpl.send(stdoutAOp(),
                        new AsyncResponseProcessor<Printer>() {
                            @Override
                            public void processAsyncResponse(
                                    final Printer _printer) {
                                _asyncRequestImpl.send(_printer.printfSOp(_format, _args), _asyncResponseProcessor);
                            }
                        });
            }
        };
    }

    static public AOp<Printer> stdoutAOp() {
        return new AOp<Printer>("stdout", Plant.getInternalFacility()) {
            @Override
            public void processAsyncOperation(final AsyncRequestImpl _asyncRequestImpl,
                                              final AsyncResponseProcessor<Printer> _asyncResponseProcessor) throws Exception {
                if (printer != null) {
                    _asyncResponseProcessor.processAsyncResponse(printer);
                    return;
                }
                printer = new Printer();
                _asyncResponseProcessor.processAsyncResponse(printer);
            }
        };
    }

    /**
     * Create a Printer blades.
     */
    private Printer() throws Exception {
        super(new BlockingReactor());
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
    public SOp<Void> printfSOp(final String _format,
            final Object... _args) {
        return new SOp<Void>("printfS", getReactor()) {
            @Override
            protected Void processSyncOperation(final RequestImpl _requestImpl) throws Exception {
                System.out.print(String.format(_format, _args));
                return null;
            }
        };
    }
}
