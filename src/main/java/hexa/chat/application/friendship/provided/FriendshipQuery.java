package hexa.chat.application.friendship.provided;

import hexa.chat.application.friendship.dto.FriendshipInfoResponse;

public interface FriendshipQuery {

    FriendshipInfoResponse memberFriendshipInfo(Long memberId);
}
