package org.thehive.hivedesktop.util.observable;

import java.util.Collection;

public interface ObserverCollection<T extends Collection<? super E>, E> extends Observer<T> {

    void onAdded(E e);

    void onRemoved(E e);

}
