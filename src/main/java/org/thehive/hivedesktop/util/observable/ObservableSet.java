package org.thehive.hivedesktop.util.observable;

import lombok.NonNull;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ObservableSet<T> extends AbstractObservable<T> implements ObservableCollection<T> {

    private final Set<T> set;

    public ObservableSet() {
        this.set = new HashSet<>();
    }

    public ObservableSet(@NonNull Set<T> set) {
        this.set = new HashSet<>(set);
    }

    @Override
    public void add(T t) {
        set.add(t);
    }

    @Override
    public void remove(T t) {
        set.remove(t);
    }

    @Override
    public int size() {
        return set.size();
    }

    @Override
    public Iterator<T> iterator() {
        return set.iterator();
    }

}
