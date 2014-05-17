package org.agilewiki.jactor2.modules.immutable;

import org.agilewiki.jactor2.modules.properties.immutable.ImmutableProperties;
import org.agilewiki.jactor2.modules.properties.immutable.SimplisticImmutableProperties;

public class SimplisticImmutablePropertiesSample {
    public static void main(final String[] args) {
        ImmutableProperties ip = SimplisticImmutableProperties.empty();
        ip = ip.plus("one", "1");
        ip = ip.plus("two", "2");
        ImmutableProperties ip2 = ip;
        ip = ip.plus("three", "3");
        System.out.println(ip2.sortedKeySet());
        System.out.println(ip.subMap("t").sortedKeySet());
    }
}
