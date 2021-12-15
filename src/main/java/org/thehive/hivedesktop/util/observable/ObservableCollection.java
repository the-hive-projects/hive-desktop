package org.thehive.hivedesktop.util.observable;

public interface ObservableCollection<T> extends Observable<T>, Iterable<T> {

    void add(T t);

    void remove(T t);

    int size();

}
