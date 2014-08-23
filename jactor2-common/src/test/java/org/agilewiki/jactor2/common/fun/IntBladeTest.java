package org.agilewiki.jactor2.common.fun;

import junit.framework.TestCase;
import org.agilewiki.jactor2.core.impl.Plant;

public class IntBladeTest extends TestCase {
    public void test1() throws Exception {
        new Plant();
        try {
            IntBlade intBlade = new IntBlade();
            new ISet(intBlade, 42).call();
            new IPrint(intBlade).call();
        } finally {
            Plant.close();
        }
    }
}
