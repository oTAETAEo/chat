package hexa.chat.adapter.jpa.chat.chatroommember;

import hexa.chat.application.chat.chatroommember.required.ChatRoomMemberRepository;
import hexa.chat.domain.chat.chatroommember.ChatRoomMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatRoomMemberRepositoryAdapter implements ChatRoomMemberRepository {

    private final ChatRoomMemberJpaRepository chatRoomMemberJpaRepository;

    @Override
    public ChatRoomMember save(ChatRoomMember chatRoomMember) {
        return chatRoomMemberJpaRepository.save(chatRoomMember);
    }

    @Override
    public List<ChatRoomMember> saveAll(List<ChatRoomMember> chatRoomMembers) {
        return chatRoomMemberJpaRepository.saveAll(chatRoomMembers);
    }

    @Override
    public List<ChatRoomMember> findAll(Long chatRoomId) {
        return chatRoomMemberJpaRepository.findAllByChatRoom_Id(chatRoomId);
    }
}
