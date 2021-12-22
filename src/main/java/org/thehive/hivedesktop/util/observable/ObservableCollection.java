package org.thehive.hivedesktop.util.observable;

import java.util.Collection;

public interface ObservableCollection<E> extends Observable<Collection<E>>, Iterable<E> {

    void add(E e);

    void addAll(Collection<? extends E> c);

    void remove(E e);

    void clear();

    int size();

    boolean contains(E e);

    Collection<E> getCollection();

}
