package hexa.chat.adapter.web.dto;

import hexa.chat.application.friendship.dto.FriendshipInfoResponse;
import hexa.chat.application.member.dto.MemberInfoPublicResponse;

public record AuthInfoResponse(

    MemberInfoPublicResponse memberInfoPublicResponse,
    FriendshipInfoResponse friendshipInfoResponse

) {

    public static AuthInfoResponse of(MemberInfoPublicResponse member, FriendshipInfoResponse friendships) {
        return new AuthInfoResponse(member, friendships);
    }
}
