package org.thehive.hivedesktop.chat;

import org.thehive.hiveserverclient.payload.ChatMessage;

public interface ChatMessageObserver {

    void onAdd(ChatMessage chatMessage);

}
