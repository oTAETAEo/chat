package hexa.chat.application.friendship.required;

import hexa.chat.domain.friendship.Friendship;

import java.util.Optional;
import java.util.UUID;

public interface FriendshipRepository{

    Friendship save(Friendship friendship);

    Optional<Friendship> findFriendshipByPublicId(UUID publicId);

    Optional<Friendship> findFriendshipRequest(Long fromMemberId, Long toMemberId);

    void delete(Friendship friendship);
}
