package hexa.chat.application.chat.chatroom.provided;

import hexa.chat.domain.chat.chatroom.ChatRoom;

import java.util.UUID;

public interface ChatRoomFinder {

    ChatRoom find(UUID publicId);

    ChatRoom find(Long id);
}
