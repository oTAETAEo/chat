package hexa.chat.adapter.jpa.chat.chatmessage;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hexa.chat.domain.chat.chatmessage.ChatMessage;
import hexa.chat.domain.chat.chatmessage.QChatMessage;
import hexa.chat.domain.member.QMember;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class ChatMessageJpaRepositoryImpl implements ChatMessageRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

}
