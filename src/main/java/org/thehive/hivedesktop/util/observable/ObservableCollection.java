package org.thehive.hivedesktop.util.observable;

import java.util.Collection;

public interface ObservableCollection<E> extends Observable<Collection<E>>, Iterable<E> {

    void add(E e);

    void remove(E e);

    int size();

    void clear();

    boolean contains(E e);

    Collection<E> getCollection();

}
