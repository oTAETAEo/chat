package hexa.chat.application.chat.chatroom.required;

import hexa.chat.domain.chat.chatroom.ChatRoom;

import java.util.Optional;
import java.util.UUID;

public interface ChatRoomRepository {

    ChatRoom save(ChatRoom chatRoom);

    Optional<ChatRoom> findByPublicId(UUID publicId);

    Optional<ChatRoom> findById(Long id);
}
