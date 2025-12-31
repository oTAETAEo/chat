package hexa.chat.application.chat;

import hexa.chat.application.chat.chatroom.required.ChatRoomRepository;
import hexa.chat.application.chat.chatroommember.required.ChatRoomMemberRepository;
import hexa.chat.application.member.required.MemberRepository;
import hexa.chat.domain.chat.ChatRoomCreateRequest;
import hexa.chat.domain.chat.chatroom.ChatRoom;
import hexa.chat.domain.chat.chatroom.ChatRoomType;
import hexa.chat.domain.chat.chatroommember.ChatRoomMember;
import hexa.chat.domain.member.Member;
import hexa.chat.domain.member.MemberFixture;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class ChatRoomCreateTest {
    @Autowired
    private ChatRoomCreate chatRoomCreate;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatRoomMemberRepository chatRoomMemberRepository;

    @DisplayName("채팅방을 생성 후 참여 회원을 구성한다.")
    @Test
    void createWithMembers() {
        // given
        Member member1 = memberRepository.save(MemberFixture.createMember("han@han.com", "han", "_han_"));
        Member member2 = memberRepository.save(MemberFixture.createMember("kim@kim.com", "kim", "_kim_"));
        memberRepository.save(member1);
        memberRepository.save(member2);

        ChatRoomCreateRequest request = new ChatRoomCreateRequest(ChatRoomType.PRIVATE, List.of(member1.getPublicId(), member2.getPublicId()));

        // when
        ChatRoom chatRoom = chatRoomCreate.createWithMembers(request);

        // then
        Optional<ChatRoom> result = chatRoomRepository.findByPublicId(chatRoom.getPublicId());
        assertThat(result).isPresent();

        List<ChatRoomMember> members = chatRoomMemberRepository.findAll(chatRoom.getId());
        assertThat(members).hasSize(2)
            .extracting(ChatRoomMember::getMember)
            .containsExactlyInAnyOrder(member1, member2);
    }

    @DisplayName("참여 회원 리스트가 비어있어 채팅방 구성에 실패 한다.")
    @Test
    void createWithMembersFail() {
        // given
        ChatRoomCreateRequest request = new ChatRoomCreateRequest(null, List.of());

        // when - then
        assertThatThrownBy(() -> chatRoomCreate.createWithMembers(request))
            .isInstanceOf(ConstraintViolationException.class);

    }

}
