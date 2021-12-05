package org.thehive.hivedesktop.chat;

import lombok.NonNull;
import org.thehive.hiveserverclient.payload.Chat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ChatObservableList extends AbstractChatObservable implements ChatList {

    private final List<Chat> chatList;

    public ChatObservableList() {
        this.chatList = new ArrayList<>();
    }

    public ChatObservableList(@NonNull Collection<? extends Chat> chats) {
        this.chatList = new ArrayList<>(chats);
    }

    @Override
    public void add(@NonNull Chat chat) {
        chatList.add(chat);
        getObservers().parallelStream()
                .forEach(observer -> observer.onAdd(chat));
    }

    @Override
    public boolean contains(@NonNull Chat chat) {
        return chatList.contains(chat);
    }

    @Override
    public int size() {
        return chatList.size();
    }

}
