package hexa.chat.application.chat.chatmessage.required;

import hexa.chat.domain.chat.chatmessage.ChatMessage;

import java.util.Optional;

public interface ChatMessageRepository {

    ChatMessage save(ChatMessage chatMessage);

    Optional<ChatMessage> findById(Long id);
}
