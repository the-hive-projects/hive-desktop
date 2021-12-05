package org.thehive.hivedesktop.chat;

import org.thehive.hiveserverclient.payload.Chat;

public interface ChatList {

    void add(Chat chat);

    boolean contains(Chat chat);

    int size();

}
