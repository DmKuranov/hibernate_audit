package ru.dmkuranov.hibernate_audit.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.Map;

public class MapFormatter<K,V> implements Iterator {
    private final Map<K,V> map;
    private final Iterator<K> iterator;
    private final EntryFormatter<K,V> entryFormatter;
    public MapFormatter(Map<K,V> map, EntryFormatter<K,V> entryFormatter) {
        this.map = map;
        iterator = this.map.keySet().iterator();
        this.entryFormatter = entryFormatter;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public Object next() {
        K key = iterator.next();
        V value = map.get(key);
        return entryFormatter.toString(key, value);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    public static interface EntryFormatter<K, V> {
        String toString(K key, V value);
    }

    public static <K,V> String format(Map<K,V> map, String separator, EntryFormatter<K, V> entryFormatter) {
        MapFormatter<K,V> mapFormatter = new MapFormatter<K, V>(map, entryFormatter);
        return StringUtils.join(mapFormatter, separator);
    }
}
