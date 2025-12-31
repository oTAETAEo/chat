package hexa.chat.domain.chat.message;

import hexa.chat.domain.chat.chatroom.ChatRoom;
import hexa.chat.domain.chat.chatroom.ChatRoomType;
import hexa.chat.domain.member.Member;
import hexa.chat.domain.member.MemberFixture;
import hexa.chat.domain.shared.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
        Member sender = MemberFixture.createMember();
        ChatRoom chatRoom = ChatRoom.register(ChatRoomType.PRIVATE);
        Message message = new Message("old text");
        ChatMessage chatMessage = ChatMessage.register(sender, message, chatRoom);

        // when
        chatMessage.modify("new text");

        // then
        assertThat(chatMessage.getChatRoom()).isEqualTo(chatRoom);
        assertThat(chatMessage.getMessage().content()).isEqualTo("new text");
    }

}