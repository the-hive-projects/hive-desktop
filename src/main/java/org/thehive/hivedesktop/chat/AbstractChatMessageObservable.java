package org.thehive.hivedesktop.chat;

import lombok.NonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractChatMessageObservable implements ChatMessageObservable {

    private final Set<ChatMessageObserver> chatMessageObserverSet;

    protected AbstractChatMessageObservable() {
        this.chatMessageObserverSet = new HashSet<>();
    }

    @Override
    public final void registerObserver(@NonNull ChatMessageObserver chatMessageObserver) {
        chatMessageObserverSet.add(chatMessageObserver);
    }

    @Override
    public final void unregisterObserver(@NonNull ChatMessageObserver chatMessageObserver) {
        chatMessageObserverSet.remove(chatMessageObserver);
    }

    protected final Collection<ChatMessageObserver> getObservers() {
        return Collections.unmodifiableCollection(chatMessageObserverSet);
    }

}
