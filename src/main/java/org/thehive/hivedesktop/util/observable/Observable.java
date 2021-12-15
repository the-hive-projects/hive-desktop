package org.thehive.hivedesktop.util.observable;

public interface Observable<T> {

    void registerObserver(Observer<? super T> observer);

    void unregisterObserver(Observer<? super T> observer);

}
