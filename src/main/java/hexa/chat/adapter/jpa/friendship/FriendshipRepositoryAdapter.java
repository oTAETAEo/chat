package hexa.chat.adapter.jpa.friendship;

import hexa.chat.application.friendship.required.FriendshipRepository;
import hexa.chat.domain.friendship.Friendship;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class FriendshipRepositoryAdapter implements FriendshipRepository {

    private final FriendshipJapRepository friendshipJapRepository;

    @Override
    public Friendship save(Friendship friendship) {
        return friendshipJapRepository.save(friendship);
    }

    @Override
    public Optional<Friendship> findFriendshipByPublicId(UUID publicId) {
        return friendshipJapRepository.findFriendshipByPublicId(publicId);
    }

    @Override
    public Optional<Friendship> findFriendshipRequest(Long fromMemberId, Long toMemberId) {
        return friendshipJapRepository.findFriendshipRequest(fromMemberId, toMemberId);
    }

    @Override
    public void delete(Friendship friendship) {
        friendshipJapRepository.delete(friendship);
    }
}
