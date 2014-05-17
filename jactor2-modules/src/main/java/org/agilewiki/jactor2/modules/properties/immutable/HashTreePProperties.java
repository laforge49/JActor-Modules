package org.agilewiki.jactor2.modules.properties.immutable;

import org.pcollections.HashPMap;
import org.pcollections.HashTreePMap;

import java.util.*;

/**
 * <p>
 * An implementation of ImmutableProperties based on pcollections.
 * </p>
 * <pre>
 * Sample:
 *
 * public class HashTreePPropertiesSample {
 *     public static void main(final String[] args) {
 *         ImmutableProperties&lt;String&gt; ip = HashTreePProperties.empty();
 *         ip = ip.plus("one", "1");
 *         ip = ip.plus("two", "2");
 *         ImmutableProperties&lt;String&gt; ip2 = ip;
 *         ip = ip.plus("three", "3");
 *         System.out.println(ip2.sortedKeySet());
 *         System.out.println(ip.subMap("t").sortedKeySet());
 *     }
 * }
 *
 * Output:
 *
 * [one, two]
 * [three, two]
 * </pre>
 */
public class HashTreePProperties implements ImmutableProperties{

    /**
     * Make an empty ImmutableProperties instance.
     *
     * @return The empty instance.
     */
    public static ImmutableProperties empty() {
        return new HashTreePProperties();
    }

    /**
     * Make an ImmutableProperties instance with a single key/value pair.
     *
     * @param key The key to be included.
     * @param value The value to be included.
     * @return The instance with one key/value pair.
     */
    public static ImmutableProperties singleton(String key, String value) {
        return new HashTreePProperties(key, value);
    }

    /**
     * Make an ImmutableProperties instance that includes a copy of a map.
     *
     * @param m      The map to be included.
     * @param <V>    The type of value.
     * @return The instance that includes the map.
     */
    public static <V> ImmutableProperties from(Map<String, String> m) {
        return new HashTreePProperties(m);
    }

    private HashPMap<String, String> base;

    private HashTreePProperties() {
        base = HashTreePMap.empty();
    }

    private HashTreePProperties(String _key, String _value) {
        base = HashTreePMap.singleton(_key, _value);
    }

    private HashTreePProperties(Map<String, String> _m) {
        base = HashTreePMap.from(_m);
    }

    private HashTreePProperties(HashPMap<String, String> immutableMap) {
        base = immutableMap;
    }

    @Override
    public ImmutableProperties minus(String key) {
        return new HashTreePProperties(base.minus(key));
    }

    @Override
    public ImmutableProperties plus(String key, String value) {
        return new HashTreePProperties(base.plus(key, value));
    }

    @Override
    public ImmutableProperties plusAll(Map<String, String> m) {
        return new HashTreePProperties(base.plusAll(m));
    }

    @Override
    public ImmutableProperties subMap(String keyPrefix) {
        HashPMap<String, String> hpm = HashTreePMap.empty();
        Iterator<Entry<String, String>> it = base.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, String> e = it.next();
            if (e.getKey().startsWith(keyPrefix))
                hpm = hpm.plus(e.getKey(), e.getValue());
        }
        return new HashTreePProperties(hpm);
    }

    @Override
    public String get(Object key) {
        return base.get(key);
    }

    @Override
    public boolean containsKey(Object key) {
        return base.containsKey(key);
    }

    @Override
    public SortedSet<String> sortedKeySet() {
        return Collections.unmodifiableSortedSet(new TreeSet<String>(base.keySet()));
    }

    @Override
    public String toString() {
        return new TreeMap<String, Object>(base).toString();
    }

    @Override
    public int size() {
        return base.size();
    }

    @Override
    public boolean isEmpty() {
        return base.isEmpty();
    }

    @Override
    public boolean containsValue(Object value) {
        return base.containsValue(value);
    }

    @Override
    public Set<String> keySet() {
        return sortedKeySet();
    }

    @Override
    public Collection<String> values() {
        return base.values();
    }

    @Override
    public Set<Entry<String, String>> entrySet() {
        return base.entrySet();
    }

    @Deprecated
    public String put(String key, String value) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public String remove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public void putAll(Map<? extends String, ? extends String> m) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public void clear() {
        throw new UnsupportedOperationException();
    }
}
