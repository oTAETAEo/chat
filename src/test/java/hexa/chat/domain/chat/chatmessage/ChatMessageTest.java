package hexa.chat.domain.chat.chatmessage;

import hexa.chat.domain.chat.chatroom.ChatRoom;
import hexa.chat.domain.chat.chatroom.ChatRoomType;
import hexa.chat.domain.member.Member;
import hexa.chat.domain.member.MemberFixture;
import hexa.chat.domain.shared.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ChatMessageTest {

    @DisplayName("채팅 메시지 객체를 생성한다.")
    @Test
    void register() {
        // given
        Member sender = MemberFixture.createMember();
        ChatRoom chatRoom = ChatRoom.register(ChatRoomType.PRIVATE);
        Message message = new Message("text");

        // when
        ChatMessage result = ChatMessage.register(sender, message, chatRoom);

        // then
        assertThat(result.getChatRoom()).isEqualTo(chatRoom);
        assertThat(result.getMessage().content()).isEqualTo("text");
    }

    @DisplayName("채팅 메시지 객체의 content를 수정한다.")
    @Test
    void modify() {
        // given
        Member sender = MemberFixture.createMember(1L);
        ChatRoom chatRoom = ChatRoom.register(ChatRoomType.PRIVATE);
        Message message = new Message("old text");
        ChatMessage chatMessage = ChatMessage.register(sender, message, chatRoom);

        // when
        chatMessage.modify(sender.getId(), "new text");

        // then
        assertThat(chatMessage.getChatRoom()).isEqualTo(chatRoom);
        assertThat(chatMessage.getMessage().content()).isEqualTo("new text");
    }

    @DisplayName("채팅 메시지 객체가 삭제 상태로 변경 된다.")
    @Test
    void delete() {
        // given
        Member sender = MemberFixture.createMember(1L);

        ChatRoom chatRoom = ChatRoom.register(ChatRoomType.PRIVATE);

        Message message = new Message("old text");

        LocalDateTime deletedAt = LocalDateTime.of(2025, 1, 1, 10, 10, 10);

        // when
        ChatMessage chatMessage = ChatMessage.register(sender, message, chatRoom);
        chatMessage.delete(sender.getId(), deletedAt);

        // then
        assertThat(chatMessage.getSoftDeleteInfo().getDeletedByMemberId()).isEqualTo(sender.getId());
        assertThat(chatMessage.getSoftDeleteInfo().getDeletedAt()).isEqualTo(deletedAt);
    }

    @DisplayName("채팅 메시지 객체를 삭제 상태로 변경 실패한다.")
    @Test
    void deleteFail() {
        // given
        Member sender = MemberFixture.createMember(1L);

        ChatRoom chatRoom = ChatRoom.register(ChatRoomType.PRIVATE);

        Message message = new Message("old text");

        LocalDateTime deletedAt = LocalDateTime.of(2025, 1, 1, 10, 10, 10);

        ChatMessage chatMessage = ChatMessage.register(sender, message, chatRoom);
        // when - then

        assertThatThrownBy(() -> chatMessage.delete(2L, deletedAt))
            .isInstanceOf(NoAccessToChatMessageException.class);
    }

}