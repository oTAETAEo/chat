package hexa.chat.domain.chat.chatmessage;

import hexa.chat.domain.AbstractEntity;
import hexa.chat.domain.chat.chatroom.ChatRoom;
import hexa.chat.domain.member.Member;
import hexa.chat.domain.shared.Message;
import hexa.chat.domain.shared.SoftDeleteInfo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    @Embedded
    private SoftDeleteInfo softDeleteInfo;

    public static ChatMessage register(Member sender, Message message, ChatRoom chatRoom){
        ChatMessage chatMessage = new ChatMessage();

        chatMessage.softDeleteInfo = new SoftDeleteInfo();
        chatMessage.message = message;
        chatMessage.sender = sender;
        chatMessage.chatRoom = chatRoom;

        return chatMessage;
    }

    public void modify(Long memberId, String newMessage){
        if (!this.sender.getId().equals(memberId)) {
            throw new NoAccessToChatMessageException("메시지 접근 권한이 없습니다 : " + memberId);
        }

        this.message = new Message(newMessage);
    }

    public void delete(Long memberId, LocalDateTime deletedAt) {
        if (!this.sender.getId().equals(memberId)) {
            throw new NoAccessToChatMessageException("메시지 접근 권한이 없습니다  : " + memberId);
        }

        this.softDeleteInfo.delete(memberId, deletedAt);
    }

}
