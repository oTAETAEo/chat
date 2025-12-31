package hexa.chat.application.chat.chatroom.required;

import hexa.chat.domain.chat.chatroom.ChatRoom;
import hexa.chat.domain.chat.chatroom.ChatRoomType;
import hexa.config.TestQueryDslConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(TestQueryDslConfig.class)
class ChatRoomRepositoryTest {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @DisplayName("채팅 방 객체를 저장한다.")
    @Test
    void save() {
        // given
        ChatRoom chatRoom = ChatRoom.register(ChatRoomType.PRIVATE);

        // when
        ChatRoom result = chatRoomRepository.save(chatRoom);

        // then
        assertThat(chatRoom.getPublicId()).isEqualTo(result.getPublicId());
    }

}