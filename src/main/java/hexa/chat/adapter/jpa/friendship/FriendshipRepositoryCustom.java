package hexa.chat.adapter.jpa.friendship;

import hexa.chat.domain.friendship.Friendship;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepositoryCustom {

    Optional<Friendship> findFriendshipRequest(Long fromMemberId, Long toMemberId);

    List<Friendship> findAllAcceptedFriendshipsByMemberId(Long memberId);

    List<Friendship> findAllPendingFriendshipsBySenderId(Long memberId);

    List<Friendship> findAllPendingFriendshipsByReceiverId(Long memberId);


}
