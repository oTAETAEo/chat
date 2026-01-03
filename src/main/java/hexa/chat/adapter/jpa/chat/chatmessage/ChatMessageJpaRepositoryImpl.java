package hexa.chat.adapter.jpa.chat.chatmessage;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChatMessageJpaRepositoryImpl implements ChatMessageRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

}
