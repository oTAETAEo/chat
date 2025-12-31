package hexa.chat.application.chat.chatroommember.required;

import hexa.chat.domain.chat.chatroommember.ChatRoomMember;

import java.util.List;

public interface ChatRoomMemberRepository {

    ChatRoomMember save(ChatRoomMember chatRoomMember);

    List<ChatRoomMember> saveAll(List<ChatRoomMember> chatRoomMembers);

    List<ChatRoomMember> findAll(Long chatRoomId);
}
