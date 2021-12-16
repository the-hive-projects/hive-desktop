package org.thehive.hivedesktop.util.observable;

import lombok.NonNull;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

public class ObservableMapWrapper<K, V> extends AbstractObservable<Map<K, V>> implements ObservableMap<K, V> {

    private final Map<K, V> map;

    public ObservableMapWrapper(@NonNull Map<K, V> map) {
        this.map = map;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void add(K k, V v) {
        map.put(k, v);
        observerIterator().forEachRemaining(o -> {
            o.onChanged(map);
            if (o instanceof MapObserver)
                ((MapObserver<K, V>) o).onAdded(k, v);
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public void remove(K k) {
        V v = map.remove(k);
        if (v != null) {
            observerIterator().forEachRemaining(o -> {
                o.onChanged(map);
                if (o instanceof MapObserver)
                    ((MapObserver<K, V>) o).onAdded(k, v);
            });
        }
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public V get(K k) {
        return map.get(k);
    }

    @Override
    public boolean containsKey(K k) {
        return map.containsKey(k);
    }

    @Override
    public boolean containsValue(V v) {
        return map.containsValue(v);
    }

    @Override
    public Map<K, V> getMap() {
        return Collections.unmodifiableMap(map);
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return map.entrySet().iterator();
    }

}
