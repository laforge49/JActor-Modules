package org.agilewiki.jactor2.modules.immutable;

import java.util.Map;
import java.util.SortedSet;

/**
 * ImmutableProperties is an immutable map with keys and values that are strings,
 * but with the addition of subMap and sortedKeySet methods.
 */
public interface ImmutableProperties extends Map<String, String> {
    /**
     * Make a virtual copy but with a key removed.
     *
     * @param key The key to be removed.
     * @return A virtual copy.
     */
    ImmutableProperties minus(String key);

    /**
     * Make a virtual copy but with the addition of a key/value pair.
     *
     * @param key   The key to be added.
     * @param value The value to be added.
     * @return The virtual copy.
     */
    ImmutableProperties plus(String key, String value);

    /**
     * Make a virtual copy that includes the key/value pairs of another map.
     *
     * @param m    The map to be included.
     * @return The virtual copy.
     */
    ImmutableProperties plusAll(Map<String, String> m);

    /**
     * Make an immutable subMap.
     *
     * @param keyPrefix The keys in the submap all start with this prefix.
     * @return An immutable subMap.
     */
    ImmutableProperties subMap(String keyPrefix);

    /**
     * Make an immutable SortedSet of the keys.
     *
     * @return An immutable SortedSet.
     */
    SortedSet<String> sortedKeySet();

    /**
     * An incompatible operation.
     */
    @Deprecated
    public void clear();

    /**
     * An incompatible operation.
     */
    @Deprecated
    public String put(String key, String value);

    /**
     * An incompatible operation.
     */
    @Deprecated
    public String remove(Object key);
}
