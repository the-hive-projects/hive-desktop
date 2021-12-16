package org.thehive.hivedesktop.util.observable;

@FunctionalInterface
public interface Observer<T> {

    void onChanged(T t);

}
