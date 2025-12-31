package hexa.chat.application.chat.chatroom.provided;

import hexa.chat.domain.chat.chatroom.ChatRoom;
import hexa.chat.domain.chat.chatroom.ChatRoomType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ChatRoomRegisterTest {

    @Autowired
    private ChatRoomRegister chatRoomRegister;

    @Autowired
    private ChatRoomFinder chatRoomFinder;

    @DisplayName("단체 채팅 방을 생성한다.")
    @Test
    void groupRoomRegister() {
        // when
        ChatRoom chatRoom = chatRoomRegister.groupRoomRegister();

        ChatRoom result = chatRoomFinder.find(chatRoom.getPublicId());

        // then
        assertThat(result.getPublicId()).isEqualTo(chatRoom.getPublicId());
        assertThat(result.getRoomType()).isEqualTo(ChatRoomType.GROUP);
    }

    @DisplayName("1대1 채팅 방을 생성한다.")
    @Test
    void privateRoomRegister() {
        // when
        ChatRoom chatRoom = chatRoomRegister.privateRoomRegister();

        ChatRoom result = chatRoomFinder.find(chatRoom.getPublicId());

        // then
        assertThat(result.getPublicId()).isEqualTo(chatRoom.getPublicId());
        assertThat(result.getRoomType()).isEqualTo(ChatRoomType.PRIVATE);
    }

}