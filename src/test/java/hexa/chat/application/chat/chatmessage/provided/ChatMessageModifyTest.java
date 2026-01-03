package hexa.chat.application.chat.chatmessage.provided;

import hexa.chat.application.chat.chatmessage.required.ChatMessageRepository;
import hexa.chat.application.chat.chatroom.required.ChatRoomRepository;
import hexa.chat.application.chat.chatroommember.required.ChatRoomMemberRepository;
import hexa.chat.application.member.required.MemberRepository;
import hexa.chat.domain.chat.chatmessage.ChatMessage;
import hexa.chat.domain.chat.chatmessage.ChatMessageModifyRequest;
import hexa.chat.domain.chat.chatmessage.NoAccessToChatMessageException;
import hexa.chat.domain.chat.chatroom.ChatRoom;
import hexa.chat.domain.chat.chatroom.ChatRoomType;
import hexa.chat.domain.chat.chatroommember.ChatRoomMember;
import hexa.chat.domain.member.Member;
import hexa.chat.domain.member.MemberFixture;
import hexa.chat.domain.shared.Message;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@SpringBootTest
@Transactional
class ChatMessageModifyTest {

    @Autowired
    private ChatMessageModify chatMessageModify;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatRoomMemberRepository chatRoomMemberRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private EntityManager entityManager;

    @DisplayName("채팅방 메시지를 삭제한다.")
    @Test
    void delete() {
        // given
        Member member1 = memberRepository.save(MemberFixture.createMember("han@han.com", "han", "_han_"));
        Member member2 = memberRepository.save(MemberFixture.createMember("kim@kim.com", "kim", "_kim_"));

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.register(ChatRoomType.PRIVATE));

        chatRoomMemberRepository.save(ChatRoomMember.register(member1, chatRoom));
        chatRoomMemberRepository.save(ChatRoomMember.register(member2, chatRoom));

        ChatMessage chatMessage = chatMessageRepository.save(ChatMessage.register(member1, new Message("hello"), chatRoom));

        // when
        chatMessageModify.delete(member1.getId(), chatMessage.getId());

        entityManager.flush();
        entityManager.clear();

        // then
        ChatMessage result = chatMessageRepository.findById(chatMessage.getId()).get();
        assertThat(result.getSoftDeleteInfo().isDeleted()).isTrue();
        assertThat(result.getSoftDeleteInfo().getDeletedByMemberId()).isEqualTo(member1.getId());
        assertThat(result.getSoftDeleteInfo().getDeletedAt()).isNotNull();
    }

    @DisplayName("접근 권한이 없어서 채팅방 메시지 삭제를 실패 한다.")
    @Test
    void deleteFail() {
        // given
        Member member1 = memberRepository.save(MemberFixture.createMember("han@han.com", "han", "_han_"));
        Member member2 = memberRepository.save(MemberFixture.createMember("kim@kim.com", "kim", "_kim_"));

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.register(ChatRoomType.PRIVATE));

        chatRoomMemberRepository.save(ChatRoomMember.register(member1, chatRoom));
        chatRoomMemberRepository.save(ChatRoomMember.register(member2, chatRoom));

        ChatMessage chatMessage = chatMessageRepository.save(ChatMessage.register(member1, new Message("hello"), chatRoom));

        // when - then
        assertThatThrownBy(() -> chatMessageModify.delete(member2.getId(), chatMessage.getId()))
            .isInstanceOf(NoAccessToChatMessageException.class);
    }

    @DisplayName("메시지를 수정한다.")
    @Test
    void modify() {
        // given
        Member member1 = memberRepository.save(MemberFixture.createMember("han@han.com", "han", "_han_"));
        Member member2 = memberRepository.save(MemberFixture.createMember("kim@kim.com", "kim", "_kim_"));

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.register(ChatRoomType.PRIVATE));

        chatRoomMemberRepository.save(ChatRoomMember.register(member1, chatRoom));
        chatRoomMemberRepository.save(ChatRoomMember.register(member2, chatRoom));

        ChatMessage chatMessage = chatMessageRepository.save(ChatMessage.register(member1, new Message("hello"), chatRoom));

        entityManager.flush();
        entityManager.clear();

        // when
        chatMessageModify.modify(member1.getId(), new ChatMessageModifyRequest(chatMessage.getId(), "new Message"));

        entityManager.flush();
        entityManager.clear();

        // then
        ChatMessage result = chatMessageRepository.findById(chatMessage.getId()).get();
        assertThat(result.getMessage().content()).isEqualTo("new Message");
        assertThat(result.getUpdatedAt()).isAfter(chatMessage.getUpdatedAt());

    }

}