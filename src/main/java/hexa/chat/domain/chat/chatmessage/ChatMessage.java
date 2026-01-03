package hexa.chat.domain.chat.chatmessage;

import hexa.chat.domain.AbstractEntity;
import hexa.chat.domain.chat.chatroom.ChatRoom;
import hexa.chat.domain.member.Member;
import hexa.chat.domain.shared.Message;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @Embedded
    private Message message;

    public static ChatMessage register(Member sender, Message message, ChatRoom chatRoom){
        ChatMessage chatMessage = new ChatMessage();

        chatMessage.message = message;
        chatMessage.sender = sender;
        chatMessage.chatRoom = chatRoom;

        return chatMessage;
    }

    public void modify(@NotNull String newMessage){
        this.message = new Message(newMessage);
    }

}
