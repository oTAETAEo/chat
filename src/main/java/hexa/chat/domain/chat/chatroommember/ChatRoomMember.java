package hexa.chat.domain.chat.chatroommember;

import hexa.chat.domain.AbstractEntity;
import hexa.chat.domain.chat.chatroom.ChatRoom;
import hexa.chat.domain.member.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
public class ChatRoomMember extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;

    public static ChatRoomMember register(Member member, ChatRoom chatRoom){
        ChatRoomMember chatRoomMember =new ChatRoomMember();

        chatRoomMember.member = member;
        chatRoomMember.chatRoom = chatRoom;

        return chatRoomMember;
    }
}
