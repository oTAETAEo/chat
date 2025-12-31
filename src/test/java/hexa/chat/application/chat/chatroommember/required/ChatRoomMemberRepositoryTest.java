package hexa.chat.application.chat.chatroommember.required;

import hexa.chat.application.chat.chatroom.required.ChatRoomRepository;
import hexa.chat.application.member.required.MemberRepository;
import hexa.chat.domain.chat.chatroom.ChatRoom;
import hexa.chat.domain.chat.chatroom.ChatRoomType;
import hexa.chat.domain.chat.chatroommember.ChatRoomMember;
import hexa.chat.domain.member.Member;
import hexa.chat.domain.member.MemberFixture;
import hexa.config.TestQueryDslConfig;
import org.aspectj.weaver.ast.Literal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestQueryDslConfig.class)
class ChatRoomMemberRepositoryTest {

    @Autowired
    private ChatRoomMemberRepository chatRoomMemberRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("채팅 방 참여 회원을 저장한다.")
    @Test
    void save() {
        // given
        Member member = MemberFixture.createMember();
        memberRepository.save(member);

        ChatRoom chatRoom = ChatRoom.register(ChatRoomType.PRIVATE);
        chatRoomRepository.save(chatRoom);

        ChatRoomMember chatRoomMember = ChatRoomMember.register(member, chatRoom);

        // when
        ChatRoomMember result = chatRoomMemberRepository.save(chatRoomMember);

        // then
        assertThat(result.getMember().getId()).isEqualTo(member.getId());
    }

    @DisplayName("채팅 방 참여 회원을 저장한다.")
    @Test
    void saveAll() {
        // given
        Member member1 = memberRepository.save(MemberFixture.createMember("han@han.com", "han", "_han_"));
        Member member2 = memberRepository.save(MemberFixture.createMember("kim@kim.com", "kim", "_kim_"));

        ChatRoom chatRoom = ChatRoom.register(ChatRoomType.PRIVATE);
        chatRoomRepository.save(chatRoom);

        ChatRoomMember chatRoomMember1 = ChatRoomMember.register(member1, chatRoom);
        ChatRoomMember chatRoomMember2 = ChatRoomMember.register(member2, chatRoom);
        List<ChatRoomMember> chatRoomMembers = List.of(chatRoomMember1, chatRoomMember2);

        // when
        List<ChatRoomMember> result = chatRoomMemberRepository.saveAll(chatRoomMembers);

        // then
        assertThat(result).hasSize(2)
            .extracting(ChatRoomMember::getMember)
            .containsExactlyInAnyOrder(member1, member2);
    }

}