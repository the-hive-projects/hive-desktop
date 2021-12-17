package org.thehive.hivedesktop.util.observable;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class ObservableCollectionWrapper<E> extends AbstractObservable<Collection<E>> implements ObservableCollection<E> {

    private final Collection<E> collection;

    public ObservableCollectionWrapper(@NonNull Collection<E> collection) {
        this.collection = collection;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void add(E e) {
        collection.add(e);
        observerIterator()
                .forEachRemaining(o -> {
                    o.onChanged(collection);
                    if (o instanceof CollectionObserver)
                        ((CollectionObserver) o).onAdded(e);
                });
    }

    @SuppressWarnings("unchecked")
    @Override
    public void remove(E e) {
        collection.remove(e);
        observerIterator()
                .forEachRemaining(o -> {
                    o.onChanged(collection);
                    if (o instanceof CollectionObserver)
                        ((CollectionObserver) o).onRemoved(e);
                });
    }

    @Override
    public int size() {
        return collection.size();
    }

    @Override
    public void clear() {
        var copyCollection = new ArrayList<>(collection);
        collection.clear();
        observerIterator()
                .forEachRemaining(o -> {
                    o.onChanged(copyCollection);
                    if (o instanceof CollectionObserver)
                        ((CollectionObserver) o).onCleared(copyCollection);
                });
    }

    @Override
    public boolean contains(E e) {
        return collection.contains(e);
    }

    @Override
    public Collection<E> getCollection() {
        return Collections.unmodifiableCollection(collection);
    }

    @Override
    public Iterator<E> iterator() {
        return collection.iterator();
    }

}
