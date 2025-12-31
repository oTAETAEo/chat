package hexa.chat.application.chat.chatroom;

import hexa.chat.application.chat.chatroom.provided.ChatRoomRegister;
import hexa.chat.application.chat.chatroom.required.ChatRoomRepository;
import hexa.chat.domain.chat.chatroom.ChatRoom;
import hexa.chat.domain.chat.chatroom.ChatRoomType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomRegisterService implements ChatRoomRegister {

    private final ChatRoomRepository chatRoomRepository;

    @Override
    public ChatRoom groupRoomRegister() {
        ChatRoom chatRoom = ChatRoom.register(ChatRoomType.GROUP);

        return chatRoomRepository.save(chatRoom);
    }

    @Override
    public ChatRoom privateRoomRegister() {
        ChatRoom chatRoom = ChatRoom.register(ChatRoomType.PRIVATE);

        return chatRoomRepository.save(chatRoom);
    }


}
