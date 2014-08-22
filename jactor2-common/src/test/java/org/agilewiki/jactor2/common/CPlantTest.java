package org.agilewiki.jactor2.common;

import junit.framework.TestCase;

public class CPlantTest extends TestCase {
    public void test1() throws Exception {
        new CPlant();
        try {
            System.out.println(":-)");
        } finally {
            CPlant.close();
        }
    }
}
