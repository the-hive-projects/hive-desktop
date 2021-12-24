package org.thehive.hivedesktop.util.observable;

import java.util.concurrent.atomic.AtomicReference;

public class ObservableUnit<E> extends AbstractObservable<E> {

    private final AtomicReference<E> reference;

    public ObservableUnit() {
        this(null);
    }

    public ObservableUnit(E e) {
        this.reference = new AtomicReference<>(e);
    }

    public E get() {
        return reference.get();
    }

    public void setValue(E e) {
        reference.set(e);
        observerIterator()
                .forEachRemaining(o -> o.onChanged(e));
    }

    public void clearValue() {
        reference.set(null);
        observerIterator()
                .forEachRemaining(o -> o.onChanged(null));
    }

}
