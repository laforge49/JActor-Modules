package org.agilewiki.jactor2.common.service;

import junit.framework.TestCase;
import org.agilewiki.jactor2.common.CPlant;

public class PrinterTest extends TestCase {
    public void test1() throws Exception {
        new CPlant();
        try {
            Printer printer = new PrinterImpl();
            printer.registerSOp().call();
            Printer x = Printer.getPrinter();
            x.printlnSOp("Ho!").call();
            x.unregisterSOp().call();
        } finally {
            CPlant.close();
        }
    }
}