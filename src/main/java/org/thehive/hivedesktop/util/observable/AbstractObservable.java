package org.thehive.hivedesktop.util.observable;

import lombok.NonNull;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public abstract class AbstractObservable<T> implements Observable<T> {

    protected final Set<Observer<? super T>> observerSet;

    protected AbstractObservable() {
        this.observerSet = new HashSet<>();
    }

    @Override
    public final void registerObserver(@NonNull Observer<? super T> observer) {
        observerSet.add(observer);
    }

    @Override
    public final void unregisterObserver(@NonNull Observer<? super T> observer) {
        observerSet.remove(observer);
    }

    protected final Iterator<Observer<? super T>> observerIterator() {
        return observerSet.iterator();
    }

}
