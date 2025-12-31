package hexa.chat.adapter.jpa.chat.chatroommember;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChatRoomMemberJpaRepositoryImpl implements ChatRoomMemberRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
}
