package org.thehive.hivedesktop.util.observable;

import java.util.Map;

public abstract class MapObserverAdapter<K, V> implements MapObserver<K, V> {

    @Override
    public void onChanged(Map<K, V> kvMap) {
    }

    @Override
    public void onAdded(K k, V v) {
    }

    @Override
    public void onRemoved(K k, V v) {
    }

    @Override
    public void onCleared(Map<K, V> map) {
    }

}
