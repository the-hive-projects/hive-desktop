package org.thehive.hivedesktop.util.observable;

public interface Observer<T> {

    void onChanged(T t);

}
