package org.thehive.hivedesktop.util.observable;

import java.util.Collection;

public interface ObservableCollection<T extends Collection<E>, E> extends Observable<T>, Iterable<E> {

    void add(E e);

    void remove(E e);

    int size();

}
