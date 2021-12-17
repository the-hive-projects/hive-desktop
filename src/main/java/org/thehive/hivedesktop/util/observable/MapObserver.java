package org.thehive.hivedesktop.util.observable;

import java.util.Map;

public interface MapObserver<K, V> extends Observer<Map<K, V>> {

    void onAdded(K k, V v);

    void onRemoved(K k, V v);

    void onCleared(Map<K, V> map);

}
