package org.thehive.hivedesktop.util.observable;

import java.util.Collection;
import java.util.Iterator;

public class WrapperObservableCollection<T extends Collection<E>, E> extends AbstractObservable<T> implements ObservableCollection<T, E> {

    private final T collection;

    public WrapperObservableCollection(T collection) {
        this.collection = collection;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void add(E e) {
        collection.add(e);
        observerIterator()
                .forEachRemaining(o -> {
                    o.onChanged(collection);
                    if (o instanceof ObserverCollection)
                        ((ObserverCollection<T, E>) o).onAdded(e);
                });
    }

    @SuppressWarnings("unchecked")
    @Override
    public void remove(E e) {
        collection.remove(e);
        observerIterator()
                .forEachRemaining(o -> {
                    o.onChanged(collection);
                    if (o instanceof ObserverCollection)
                        ((ObserverCollection<T, E>) o).onRemoved(e);
                });
    }

    @Override
    public int size() {
        return collection.size();
    }

    @Override
    public Iterator<E> iterator() {
        return collection.iterator();
    }

}
