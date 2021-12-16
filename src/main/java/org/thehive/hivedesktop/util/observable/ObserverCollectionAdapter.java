package org.thehive.hivedesktop.util.observable;

import java.util.Collection;

public abstract class ObserverCollectionAdapter<T extends Collection<? super E>, E> implements ObserverCollection<T, E> {

    @Override
    public void onChanged(T t) {
    }

    @Override
    public void onAdded(E e) {
    }

    @Override
    public void onRemoved(E e) {
    }

}
