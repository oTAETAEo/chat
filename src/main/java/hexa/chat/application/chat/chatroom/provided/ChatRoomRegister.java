package hexa.chat.application.chat.chatroom.provided;

import hexa.chat.domain.chat.chatroom.ChatRoom;
import hexa.chat.domain.chat.chatroom.ChatRoomType;

public interface ChatRoomRegister {

    ChatRoom groupRoomRegister();

    ChatRoom privateRoomRegister();
}
