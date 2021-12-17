package org.thehive.hivedesktop.util.observable;

import java.util.Map;

public interface ObservableMap<K, V> extends Observable<Map<K, V>>, Iterable<Map.Entry<K, V>> {

    void add(K k, V v);

    void remove(K k);

    int size();

    V get(K k);

    void clear();

    boolean containsKey(K k);

    boolean containsValue(V v);

    Map<K, V> getMap();

}
