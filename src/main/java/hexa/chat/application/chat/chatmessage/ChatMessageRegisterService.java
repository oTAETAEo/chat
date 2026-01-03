package hexa.chat.application.chat.chatmessage;

import hexa.chat.application.chat.chatmessage.provided.ChatMessageRegister;
import hexa.chat.application.chat.chatmessage.required.ChatMessageRepository;
import hexa.chat.domain.chat.chatmessage.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatMessageRegisterService implements ChatMessageRegister {

    private final ChatMessageRepository chatMessageRepository;

    @Override
    public ChatMessage register(ChatMessage chatMessage) {
        return chatMessageRepository.save(chatMessage);
    }
}
