package hexa.chat.application.friendship.dto;

import hexa.chat.domain.friendship.Friendship;

import java.util.List;

public record FriendshipInfoResponse(

    List<FriendshipResponse> friendships,
    List<FriendshipResponse> senderPendingFriendships,
    List<FriendshipResponse> receivePendingFriendships

) {
    public FriendshipInfoResponse {
        senderPendingFriendships = List.copyOf(senderPendingFriendships);
        friendships = List.copyOf(friendships);
        receivePendingFriendships = List.copyOf(receivePendingFriendships);
    }
}
