package hexa.chat.domain.chat.chatroom;

import hexa.chat.domain.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import org.hibernate.annotations.NaturalId;

import java.util.UUID;

@Entity
@Getter
public class ChatRoom extends AbstractEntity {

    @NaturalId
    @Column(unique = true, nullable = false)
    private UUID publicId;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private ChatRoomType roomType;

    public static ChatRoom register(ChatRoomType type){
        ChatRoom chatRoom =new ChatRoom();

        chatRoom.roomType = type;
        chatRoom.publicId = UUID.randomUUID();

        return chatRoom;
    }

}
