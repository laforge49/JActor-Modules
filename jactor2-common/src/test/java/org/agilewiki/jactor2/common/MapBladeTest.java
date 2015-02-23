package org.agilewiki.jactor2.common;

import junit.framework.TestCase;
import org.agilewiki.jactor2.core.blades.NonBlockingBladeBase;
import org.agilewiki.jactor2.core.impl.Plant;
import org.agilewiki.jactor2.core.messages.SOp;
import org.agilewiki.jactor2.core.messages.impl.RequestImpl;

import java.util.HashMap;
import java.util.Map;

public class MapBladeTest extends TestCase {
    public void test1() throws Exception {
        new Plant();
        try {
            final MapBlade<String, Integer> mapBlade = new MapBlade();

            new MapBlade.MapBladeSOp<Void, String, Integer>("initializeMapBlade", mapBlade) {
                @Override
                protected Void processSyncOperation(RequestImpl _requestImpl)
                        throws Exception {
                    Map<String, Integer> m = new HashMap();
                    setMap(m);
                    m.put("x", 42);
                    return null;
                }
            }.call();

            new MapBlade.MapBladeSOp<Void, String, Integer>("printX", mapBlade) {
                @Override
                protected Void processSyncOperation(RequestImpl _requestImpl)
                        throws Exception {
                    System.out.println(getMap().get("x"));
                    return null;
                }
            }.call();
        } finally {
            Plant.close();
        }
    }
}

class MapBlade<K, V> extends NonBlockingBladeBase {
    private Map<K, V> m;

    public MapBlade() throws Exception {
    }

    public static abstract class MapBladeSOp<RESPONSE_TYPE, K, V> extends SOp<RESPONSE_TYPE> {
        protected final MapBlade<K, V> mapBlade;

        public MapBladeSOp(String _opName, MapBlade<K, V> _mapBlade) {
            super(_opName, _mapBlade.getReactor());
            mapBlade = _mapBlade;
        }

        protected Map<K, V> getMap() {
            return mapBlade.m;
        }

        protected void setMap(Map<K, V> _m) {
            mapBlade.m = _m;
        }
    }
}
