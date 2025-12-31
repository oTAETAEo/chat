package hexa.chat.application.chat.chatroommember.provided;

import hexa.chat.domain.chat.chatroommember.ChatRoomMember;

import java.util.List;
import java.util.UUID;

public interface ChatRoomMemberRegister {

    List<ChatRoomMember> register(Long chatRoomId, List<UUID> memberIds);
}
