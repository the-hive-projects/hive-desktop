package org.thehive.hivedesktop.chat;

import org.thehive.hiveserverclient.payload.Chat;

public interface ChatObservable {

    void add(Chat chat);

    void registerObserver(ChatObserver chatObserver);

    void unregisterObserver(ChatObserver chatObserver);

}
