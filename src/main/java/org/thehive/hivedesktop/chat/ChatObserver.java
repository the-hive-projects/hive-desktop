package org.thehive.hivedesktop.chat;

import org.thehive.hiveserverclient.payload.Chat;

public interface ChatObserver {

    void onAdd(Chat chat);

}
