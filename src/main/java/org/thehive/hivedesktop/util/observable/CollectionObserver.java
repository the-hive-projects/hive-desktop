package org.thehive.hivedesktop.util.observable;

import java.util.Collection;

public interface CollectionObserver<E> extends Observer<Collection<E>> {

    void onAdded(E e);

    void onRemoved(E e);

}
