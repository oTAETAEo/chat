package hexa.chat.application.chat.chatmessage.provided;

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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ChatMessageRegisterTest {

    @Autowired
    private ChatMessageRegister chatMessageRegister;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatRoomMemberRepository chatRoomMemberRepository;

    @DisplayName("채팅방 메시지를 저장한다")
    @Test
    void register() {
        // given
        Member member1 = MemberFixture.createMember("han@han.com", "han", "_han_");
        Member member2 = MemberFixture.createMember("kim@kim.com", "kim", "_kim_");
        member1 = memberRepository.save(member1);
        member2 = memberRepository.save(member2);

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.register(ChatRoomType.PRIVATE));

        chatRoomMemberRepository.save(ChatRoomMember.register(member1, chatRoom));
        chatRoomMemberRepository.save(ChatRoomMember.register(member2, chatRoom));

        ChatMessage message = ChatMessage.register(member1, new Message("hello"), chatRoom);

        // when
        ChatMessage result = chatMessageRegister.register(message);

        // then
        assertThat(result.getMessage().content()).isEqualTo("hello");
    }

}