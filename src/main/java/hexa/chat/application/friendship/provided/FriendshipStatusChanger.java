package hexa.chat.application.friendship.provided;

import hexa.chat.domain.friendship.Friendship;
import hexa.chat.domain.friendship.FriendshipStatusChangeRequest;

public interface FriendshipStatusChanger {

    Friendship accept(Long currentMemberId, FriendshipStatusChangeRequest request);
    Friendship block(Long currentMemberId, FriendshipStatusChangeRequest request);
    void reject(Long currentMemberId, FriendshipStatusChangeRequest request);
}
