package hexa.chat.application.chat.chatmessage;

import hexa.chat.application.chat.chatmessage.provided.ChatMessageFinder;
import hexa.chat.application.chat.chatmessage.provided.ChatMessageModify;
import hexa.chat.domain.chat.chatmessage.ChatMessage;
import hexa.chat.domain.chat.chatmessage.ChatMessageModifyRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatMessageModifyService implements ChatMessageModify {

    private final ChatMessageFinder chatMessageFinder;

    @Override
    public void delete(Long memberId, Long chatMessageId) {

        ChatMessage chatMessage = chatMessageFinder.find(chatMessageId);

        chatMessage.delete(memberId, LocalDateTime.now());
    }

    @Override
    public void modify(Long memberId, ChatMessageModifyRequest request) {

        ChatMessage chatMessage = chatMessageFinder.find(request.chatMessageId());

        chatMessage.modify(memberId, request.newMessage());
    }

}
