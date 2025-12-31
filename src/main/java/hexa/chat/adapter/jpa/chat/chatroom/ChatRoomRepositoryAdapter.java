package hexa.chat.adapter.jpa.chat.chatroom;

import hexa.chat.application.chat.chatroom.required.ChatRoomRepository;
import hexa.chat.domain.chat.chatroom.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryAdapter implements ChatRoomRepository {

    private final ChatRoomJpaRepository chatRoomJpaRepository;

    @Override
    public ChatRoom save(ChatRoom chatRoom) {
        return chatRoomJpaRepository.save(chatRoom);
    }

    @Override
    public Optional<ChatRoom> findByPublicId(UUID publicId) {
        return chatRoomJpaRepository.findChatRoomByPublicId(publicId);
    }

    @Override
    public Optional<ChatRoom> findById(Long id) {
        return chatRoomJpaRepository.findById(id);
    }
}
