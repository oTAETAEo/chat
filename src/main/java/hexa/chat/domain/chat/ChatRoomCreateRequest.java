package hexa.chat.domain.chat;

import hexa.chat.domain.chat.chatroom.ChatRoomType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record ChatRoomCreateRequest(

    @NotNull
    ChatRoomType chatRoomType,

    @NotEmpty
    List<@NotNull UUID> members
) {

    public ChatRoomCreateRequest(ChatRoomType chatRoomType, List<UUID> members) {
        this.chatRoomType = chatRoomType;
        this.members = members == null ? List.of() : List.copyOf(members);
    }

}
