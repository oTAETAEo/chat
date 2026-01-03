package hexa.chat.application.chat.chatmessage.provided;

import hexa.chat.domain.chat.chatmessage.ChatMessageModifyRequest;

public interface ChatMessageModify {

    void delete(Long memberId, Long chatMessageId);

    void modify(Long memberId, ChatMessageModifyRequest request);
}
