package hexa.chat.application.chat.chatroom.provided;

import hexa.chat.domain.chat.chatroom.ChatRoom;

public interface ChatRoomRegister {

    ChatRoom groupRoomRegister();

    ChatRoom privateRoomRegister();
}
