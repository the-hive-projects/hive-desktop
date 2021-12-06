package org.thehive.hivedesktop.chat;

import lombok.NonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractChatObservable implements ChatObservable {

    private final Set<ChatObserver> chatObserverSet;

    protected AbstractChatObservable() {
        this.chatObserverSet = new HashSet<>();
    }

    @Override
    public final void registerObserver(@NonNull ChatObserver chatObserver) {
        chatObserverSet.add(chatObserver);
    }

    @Override
    public final void unregisterObserver(@NonNull ChatObserver chatObserver) {
        chatObserverSet.remove(chatObserver);
    }

    protected final Collection<ChatObserver> getObservers() {
        return Collections.unmodifiableCollection(chatObserverSet);
    }

}
