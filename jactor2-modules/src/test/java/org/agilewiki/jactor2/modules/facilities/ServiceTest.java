package org.agilewiki.jactor2.modules.facilities;

import junit.framework.TestCase;
import org.agilewiki.jactor2.core.blades.NonBlockingBladeBase;
import org.agilewiki.jactor2.core.impl.Plant;
import org.agilewiki.jactor2.core.reactors.NonBlockingReactor;
import org.agilewiki.jactor2.core.reactors.ReactorClosedException;
import org.agilewiki.jactor2.core.requests.AOp;
import org.agilewiki.jactor2.core.requests.AsyncResponseProcessor;
import org.agilewiki.jactor2.core.requests.ExceptionHandler;
import org.agilewiki.jactor2.core.requests.impl.AsyncRequestImpl;
import org.agilewiki.jactor2.modules.MFacility;
import org.agilewiki.jactor2.modules.MPlant;

public class ServiceTest extends TestCase {
    public void test() throws Exception {
        new MPlant();
        final MFacility clientMFacility = MPlant.createMFacilityAOp("Client")
                .call();
        final MFacility serverMFacility = MPlant.createMFacilityAOp("Server")
                .call();
        try {
            NonBlockingReactor serverReactor = new NonBlockingReactor(serverMFacility);
            final Server server = new Server(serverReactor);
            NonBlockingReactor clientReactor = new NonBlockingReactor(clientMFacility);
            final Client client = new Client(clientReactor, server);
            NonBlockingReactor testReactor = new NonBlockingReactor();
            new AOp<Void>("bingo", testReactor) {
                @Override
                protected void processAsyncOperation(final AsyncRequestImpl _asyncRequestImpl,
                                                     final AsyncResponseProcessor<Void> _asyncResponseProcessor)
                        throws Exception {
                    _asyncRequestImpl.send(client.crossAOp(),
                            new AsyncResponseProcessor<Boolean>() {
                                @Override
                                public void processAsyncResponse(
                                        final Boolean response) throws Exception {
                                    Thread.sleep(10);
                                    System.out.println("Bingo!");
                                    Thread.sleep(10);
                                    assertFalse(response);
                                    _asyncResponseProcessor.processAsyncResponse(null);
                                }
                            });
                }
            }.signal();
            Thread.sleep(100);
            //serverReactor.close();     //this works
            serverMFacility.close();  //this also works
        } finally {
            Plant.close();
            Thread.sleep(500);
        }
    }
}

class Client extends NonBlockingBladeBase {

    Server server;

    Client(final NonBlockingReactor reactor, final Server _server) throws Exception {
        super(reactor);
        server = _server;
    }

    AOp<Boolean> crossAOp() {
        return new AOp<Boolean>("cross", getReactor()) {
            @Override
            protected void processAsyncOperation(final AsyncRequestImpl _asyncRequestImpl,
                                                 final AsyncResponseProcessor<Boolean> _asyncResponseProcessor)
                    throws Exception {
                _asyncRequestImpl.setExceptionHandler(new ExceptionHandler<Boolean>() {
                    @Override
                    public Boolean processException(final Exception exception)
                            throws Exception {
                        System.out.println("client got exception");
                        if (!(exception instanceof ReactorClosedException)) {
                            throw exception;
                        }
                        return false;
                    }
                });
                Thread.sleep(10);
                AOp rb = server.hangAOp();
                System.out.println("client send hang " + rb);
                Thread.sleep(10);
                _asyncRequestImpl.send(rb, _asyncResponseProcessor, true);
            }
        };
    }
}

class Server extends NonBlockingBladeBase {
    Server(final NonBlockingReactor reactor) throws Exception {
        super(reactor);
    }

    AOp<Void> hangAOp() {
        return new AOp<Void>("hang", getReactor()) {
            @Override
            protected void processAsyncOperation(final AsyncRequestImpl _asyncRequestImpl,
                                                 final AsyncResponseProcessor<Void> _asyncResponseProcessor)
                    throws Exception {
            }
        };
    }
}
