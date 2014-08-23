package org.agilewiki.jactor2.common.service;

import org.agilewiki.jactor2.core.reactors.BlockingReactor;
import org.agilewiki.jactor2.core.requests.SOp;
import org.agilewiki.jactor2.core.requests.impl.RequestImpl;

public class PrinterServiceImpl extends PrinterService {

    public static void register() throws Exception {
        new PrinterServiceImpl().registerSOp().call();
    }

    public PrinterServiceImpl() throws Exception {
        this(new BlockingReactor());
    }

    public PrinterServiceImpl(BlockingReactor _reactor) {
        super(_reactor);
    }

    @Override
    public SOp<Void> printlnSOp(final String _string) {
        return new SOp<Void>("printlnS", getReactor()) {
            @Override
            protected Void processSyncOperation(final RequestImpl _requestImpl) throws Exception {
                System.out.println(_string);
                return null;
            }
        };
    }

    @Override
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
