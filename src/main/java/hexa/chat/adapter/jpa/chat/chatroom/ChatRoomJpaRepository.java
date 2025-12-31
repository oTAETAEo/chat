package hexa.chat.adapter.jpa.chat.chatroom;

import hexa.chat.adapter.jpa.chat.chatroommember.ChatRoomMemberRepositoryCustom;
import hexa.chat.domain.chat.chatroom.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoom, Long>, ChatRoomMemberRepositoryCustom {

    Optional<ChatRoom> findChatRoomByPublicId(UUID publicId);
}
