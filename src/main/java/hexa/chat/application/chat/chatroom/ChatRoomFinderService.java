package hexa.chat.application.chat.chatroom;

import hexa.chat.application.chat.chatroom.provided.ChatRoomFinder;
import hexa.chat.application.chat.chatroom.required.ChatRoomRepository;
import hexa.chat.domain.chat.chatroom.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomFinderService implements ChatRoomFinder {

    private final ChatRoomRepository chatRoomRepository;

    @Override
    public ChatRoom find(UUID publicId) {
        return chatRoomRepository.findByPublicId(publicId)
            .orElseThrow(() -> new NoSuchElementException("채팅방을 찾을 수 없습니다."));
    }

    @Override
    public ChatRoom find(Long id) {
        return chatRoomRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("채팅방을 찾을 수 없습니다."));
    }


}
