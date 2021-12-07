package org.thehive.hivedesktop.chat;

import org.thehive.hiveserverclient.payload.ChatMessage;

public interface ChatMessageObservable {

    void add(ChatMessage chatMessage);

    void registerObserver(ChatMessageObserver chatMessageObserver);

    void unregisterObserver(ChatMessageObserver chatMessageObserver);

}
