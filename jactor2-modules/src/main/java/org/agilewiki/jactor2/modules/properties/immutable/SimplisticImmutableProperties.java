package org.agilewiki.jactor2.modules.properties.immutable;

import java.util.*;

/**
 * <p>
 * A brute-force implementation of ImmutableProperties.
 * </p>
 * <pre>
 * Sample:
 *
 * public class SimplisticImmutablePropertiesSample {
 *     public static void main(final String[] args) {
 *         ImmutableProperties&lt;String&gt; ip = SimplisticImmutableProperties.empty();
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
 *
 */
public class SimplisticImmutableProperties implements ImmutableProperties {
    /**
     * Make an empty ImmutableProperties instance.
     *
     * @return The empty instance.
     */
    public static ImmutableProperties empty() {
        return new SimplisticImmutableProperties();
    }

    /**
     * Make an ImmutableProperties instance with a single key/value pair.
     *
     * @param key   The key to be included.
     * @param value The value to be included.
     * @return The instance with one key/value pair.
     */
    public static ImmutableProperties singleton(String key, String value) {
        return new SimplisticImmutableProperties(key, value);
    }

    /**
     * Make an ImmutableProperties instance that includes a copy of a map.
     *
     * @param m   The map to be included.
     * @return The instance that includes the map.
     */
    public static ImmutableProperties from(Map<String, String> m) {
        return new SimplisticImmutableProperties(m);
    }

    private final SortedMap<String, String> base;

    private SimplisticImmutableProperties() {
        base = Collections.unmodifiableSortedMap(new TreeMap<String, String>());
    }

    private SimplisticImmutableProperties(String key, String value) {
        TreeMap<String, String> tm = new TreeMap<String, String>();
        tm.put(key, value);
        base = Collections.unmodifiableSortedMap(tm);
    }

    private SimplisticImmutableProperties(Map<String, String> m) {
        base = Collections.unmodifiableSortedMap(new TreeMap<String, String>(m));
    }

    private SimplisticImmutableProperties(SortedMap<String, String> immutableMap) {
        base = Collections.unmodifiableSortedMap(immutableMap);
    }

    @Override
    public ImmutableProperties minus(String key) {
        TreeMap<String, String> tm = new TreeMap<String, String>(base);
        tm.remove(key);
        return new SimplisticImmutableProperties(tm);
    }

    @Override
    public ImmutableProperties plus(String key, String value) {
        TreeMap<String, String> tm = new TreeMap<String, String>(base);
        tm.put(key, value);
        return new SimplisticImmutableProperties(tm);
    }

    @Override
    public ImmutableProperties plusAll(Map<String, String> m) {
        TreeMap<String, String> tm = new TreeMap<String, String>(base);
        tm.putAll(m);
        return new SimplisticImmutableProperties(tm);
    }

    @Override
    public ImmutableProperties subMap(String keyPrefix) {
        return new SimplisticImmutableProperties(base.subMap(keyPrefix, keyPrefix + Character.MAX_VALUE));
    }

    @Override
    public String get(Object key) {
        return base.get(key);
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
    public Set<String> keySet() {
        return sortedKeySet();
    }

    @Override
    public Collection<String> values() {
        return Collections.unmodifiableCollection(base.values());
    }

    @Override
    public Set<Entry<String, String>> entrySet() {
        return Collections.unmodifiableSet(base.entrySet());
    }

    @Override
    public boolean containsValue(Object value) {
        return base.containsValue(value);
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
