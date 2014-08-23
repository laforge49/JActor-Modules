package org.agilewiki.jactor2.common.service;

import junit.framework.TestCase;
import org.agilewiki.jactor2.common.CPlant;

public class PrinterServiceTest extends TestCase {
    public void test1() throws Exception {
        new CPlant();
        try {
            PrinterService.register();
            PrinterService x = PrinterService.getPrinterService();
            x.printlnSOp("Ho!").call();
            x.unregisterSOp().call();
        } finally {
            CPlant.close();
        }
    }
}
