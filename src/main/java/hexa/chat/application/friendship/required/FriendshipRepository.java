package hexa.chat.application.friendship.required;

import hexa.chat.domain.friendship.Friendship;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FriendshipRepository{

    Friendship save(Friendship friendship);

    Optional<Friendship> findFriendshipByPublicId(UUID publicId);

    Optional<Friendship> findFriendshipRequest(Long fromMemberId, Long toMemberId);

    List<Friendship> findAllAcceptedFriendshipsByMemberId(Long memberId);

    List<Friendship> findAllPendingFriendshipsBySenderId(Long memberId);

    List<Friendship> findAllPendingFriendshipsByReceiverId(Long memberId);

    void delete(Friendship friendship);
}
