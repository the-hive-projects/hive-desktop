package org.thehive.hivedesktop.chat;

import lombok.NonNull;
import org.thehive.hiveserverclient.payload.ChatMessage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ChatMessageObservableMessageList extends AbstractChatMessageObservable implements ChatMessageList {

    private final List<ChatMessage> chatList;

    public ChatMessageObservableMessageList() {
        this.chatList = new ArrayList<>();
    }

    public ChatMessageObservableMessageList(@NonNull Collection<? extends ChatMessage> chatMessages) {
        this.chatList = new ArrayList<>(chatMessages);
    }

    @Override
    public void add(@NonNull ChatMessage chatMessage) {
        chatList.add(chatMessage);
        getObservers().parallelStream()
                .forEach(observer -> observer.onAdd(chatMessage));
    }

    @Override
    public boolean contains(@NonNull ChatMessage chatMessage) {
        return chatList.contains(chatMessage);
    }

    @Override
    public int size() {
        return chatList.size();
    }

}
