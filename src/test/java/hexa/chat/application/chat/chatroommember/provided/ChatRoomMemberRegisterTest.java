package hexa.chat.application.chat.chatroommember.provided;

import hexa.chat.application.chat.chatroom.required.ChatRoomRepository;
import hexa.chat.application.member.required.MemberRepository;
import hexa.chat.domain.chat.chatroom.ChatRoom;
import hexa.chat.domain.chat.chatroom.ChatRoomType;
import hexa.chat.domain.chat.chatroommember.ChatRoomMember;
import hexa.chat.domain.member.Member;
import hexa.chat.domain.member.MemberFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ChatRoomMemberRegisterTest {

    @Autowired
    private ChatRoomMemberRegister chatRoomMemberRegister;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("채팅 방 참여 회원을 저장한다.")
    @Test
    void register() {
        // given
        Member member1 = memberRepository.save(MemberFixture.createMember("han@han.com", "han", "_han_"));
        Member member2 = memberRepository.save(MemberFixture.createMember("kim@kim.com", "kim", "_kim_"));

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.register(ChatRoomType.PRIVATE));

        // when
        List<ChatRoomMember> result = chatRoomMemberRegister.register(chatRoom.getId(), List.of(member1.getPublicId(), member2.getPublicId()));

        // then
        assertThat(result).hasSize(2)
            .extracting(ChatRoomMember::getMember)
            .containsExactlyInAnyOrder(member1, member2);
    }

}