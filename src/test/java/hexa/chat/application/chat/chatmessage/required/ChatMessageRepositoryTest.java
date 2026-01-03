package hexa.chat.application.chat.chatmessage.required;

import hexa.chat.application.chat.chatroom.required.ChatRoomRepository;
import hexa.chat.application.chat.chatroommember.required.ChatRoomMemberRepository;
import hexa.chat.application.member.required.MemberRepository;
import hexa.chat.domain.chat.chatmessage.ChatMessage;
import hexa.chat.domain.chat.chatroom.ChatRoom;
import hexa.chat.domain.chat.chatroom.ChatRoomType;
import hexa.chat.domain.chat.chatroommember.ChatRoomMember;
import hexa.chat.domain.member.Member;
import hexa.chat.domain.member.MemberFixture;
import hexa.chat.domain.shared.Message;
import hexa.config.TestQueryDslConfig;
import org.aspectj.weaver.ast.Literal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestQueryDslConfig.class)
class ChatMessageRepositoryTest {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatRoomMemberRepository chatRoomMemberRepository;

    @DisplayName("채팅방 메시지 객체가 저장된다.")
    @Test
    void save() {
        // given
        Member member1 = MemberFixture.createMember("test@test.com", "test", "_test_");
        Member member2 = MemberFixture.createMember("test@test.com", "test", "_test_");
        member1 = memberRepository.save(member1);
        member2 = memberRepository.save(member2);

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.register(ChatRoomType.PRIVATE));

        chatRoomMemberRepository.save(ChatRoomMember.register(member1, chatRoom));
        chatRoomMemberRepository.save(ChatRoomMember.register(member2, chatRoom));

        ChatMessage message = ChatMessage.register(member1, new Message("hello"), chatRoom);

        // when
        message = chatMessageRepository.save(message);

        // then
        assertThat(message.getMessage().content()).isEqualTo("hello");
    }

}