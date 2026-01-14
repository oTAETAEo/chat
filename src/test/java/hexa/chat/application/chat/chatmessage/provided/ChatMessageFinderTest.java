package hexa.chat.application.chat.chatmessage.provided;

import hexa.chat.application.chat.chatmessage.required.ChatMessageRepository;
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
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ChatMessageFinderTest {

    @Autowired
    private ChatMessageFinder chatMessageFinder;

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

    @DisplayName("채팅방 메시지를 조회한다.")
    @Test
    void find() {
        // given
        Member member1 = memberRepository.save(MemberFixture.createMember("han@han.com", "han", "_han_"));
        Member member2 = memberRepository.save(MemberFixture.createMember("kim@kim.com", "kim", "_kim_"));

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.register(ChatRoomType.PRIVATE));

        chatRoomMemberRepository.save(ChatRoomMember.register(member1, chatRoom));
        chatRoomMemberRepository.save(ChatRoomMember.register(member2, chatRoom));

        ChatMessage chatMessage = chatMessageRepository
                .save(ChatMessage.register(member1, new Message("hello"), chatRoom));

        entityManager.flush();
        entityManager.clear();

        // when
        ChatMessage result = chatMessageFinder.find(chatMessage.getId());

        // then
        assertThat(result.getId()).isEqualTo(chatMessage.getId());
        assertThat(result.getSender().getName()).isEqualTo(member1.getName());
    }

    @DisplayName("삭제된 메시지는 조회할 수 없다.")
    @Test
    void find_deleted_message() {
        // given
        Member member1 = memberRepository.save(MemberFixture.createMember("han@han.com", "han", "_han_"));
        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.register(ChatRoomType.PRIVATE));
        chatRoomMemberRepository.save(ChatRoomMember.register(member1, chatRoom));
        ChatMessage chatMessage = chatMessageRepository
                .save(ChatMessage.register(member1, new Message("hello"), chatRoom));

        chatMessage.delete(member1.getId(), java.time.LocalDateTime.now());
        entityManager.flush();
        entityManager.clear();

        // when - then
        assertThatThrownBy(() -> chatMessageFinder.find(chatMessage.getId()))
                .isInstanceOf(NoSuchElementException.class);
    }

}