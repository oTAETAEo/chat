package hexa.chat.application.chat;

import hexa.chat.application.chat.chatroom.ChatRoomRegisterService;
import hexa.chat.application.chat.chatroommember.ChatRoomMemberRegisterService;
import hexa.chat.domain.chat.ChatRoomCreateRequest;
import hexa.chat.domain.chat.chatroom.ChatRoom;
import hexa.chat.domain.chat.chatroom.ChatRoomType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class CreateChatRoomWithMembersService implements ChatRoomCreate{

    private final ChatRoomRegisterService chatRoomRegisterService;
    private final ChatRoomMemberRegisterService chatRoomMemberRegisterService;

    @Override
    public ChatRoom createWithMembers(ChatRoomCreateRequest request){
        ChatRoom chatRoom = createChatRoom(request);

        chatRoomMemberRegisterService.register(chatRoom.getId(), request.members());

        return chatRoom;
    }

    private ChatRoom createChatRoom(ChatRoomCreateRequest request) {
        if (request.chatRoomType() == ChatRoomType.PRIVATE){
            return chatRoomRegisterService.privateRoomRegister();
        }

        return chatRoomRegisterService.groupRoomRegister();
    }

}
