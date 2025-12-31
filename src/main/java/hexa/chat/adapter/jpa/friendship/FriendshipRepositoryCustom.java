package hexa.chat.adapter.jpa.friendship;

import hexa.chat.domain.friendship.Friendship;

import java.util.Optional;

public interface FriendshipRepositoryCustom {

    Optional<Friendship> findFriendshipRequest(Long fromMemberId, Long toMemberId);
}
