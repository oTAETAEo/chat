package hexa.chat.application.chat.chatmessage.provided;

import hexa.chat.domain.chat.chatmessage.ChatMessage;

public interface ChatMessageFinder {

    ChatMessage find(Long chatMessageId);

}
