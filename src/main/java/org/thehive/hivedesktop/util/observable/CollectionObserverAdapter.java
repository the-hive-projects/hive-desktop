package org.thehive.hivedesktop.util.observable;

import java.util.Collection;

public abstract class CollectionObserverAdapter<E> implements CollectionObserver<E> {

    @Override
    public void onChanged(Collection<E> c) {
    }

    @Override
    public void onAdded(E e) {
    }

    @Override
    public void onRemoved(E e) {
    }

}
