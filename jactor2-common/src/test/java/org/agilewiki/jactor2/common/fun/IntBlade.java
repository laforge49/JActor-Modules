package org.agilewiki.jactor2.common.fun;

import org.agilewiki.jactor2.core.blades.NonBlockingBladeBase;
import org.agilewiki.jactor2.core.requests.SOp;

public class IntBlade extends NonBlockingBladeBase {
    private int i;

    public IntBlade() throws Exception {}

    public static abstract class IntBladeSOp<RESPONSE_TYPE> extends SOp<RESPONSE_TYPE> {
        protected final IntBlade intBlade;

        public IntBladeSOp(String _opName, IntBlade _intBlade) {
            super(_opName, _intBlade.getReactor());
            intBlade = _intBlade;
        }

        protected int getInt() {
            return intBlade.i;
        }

        protected void setInt(int _i) {
            intBlade.i = _i;
        }
    }
}
