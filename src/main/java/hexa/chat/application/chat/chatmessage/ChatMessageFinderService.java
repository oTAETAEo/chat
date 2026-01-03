package hexa.chat.application.chat.chatmessage;

import hexa.chat.application.chat.chatmessage.provided.ChatMessageFinder;
import hexa.chat.application.chat.chatmessage.required.ChatMessageRepository;
import hexa.chat.domain.chat.chatmessage.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatMessageFinderService implements ChatMessageFinder {

    private final ChatMessageRepository chatMessageRepository;

    @Override
    public ChatMessage find(Long chatMessageId) {
        return chatMessageRepository.findById(chatMessageId)
                .filter(chatMessage -> !chatMessage.getSoftDeleteInfo().isDeleted())
                .orElseThrow();
    }

}
