package hexa.chat.application.chat;

import hexa.chat.domain.chat.ChatRoomCreateRequest;
import hexa.chat.domain.chat.chatroom.ChatRoom;
import jakarta.validation.Valid;

public interface ChatRoomCreate {
    ChatRoom createWithMembers(@Valid ChatRoomCreateRequest request);
}
