package org.thehive.hivedesktop.chat;

import org.thehive.hiveserverclient.payload.ChatMessage;

public interface ChatMessageList {

    void add(ChatMessage chatMessage);

    boolean contains(ChatMessage chatMessage);

    int size();

}
