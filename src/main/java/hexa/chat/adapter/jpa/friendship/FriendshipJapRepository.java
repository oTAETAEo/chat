package hexa.chat.adapter.jpa.friendship;

import hexa.chat.domain.friendship.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FriendshipJapRepository extends JpaRepository<Friendship, Long>, FriendshipRepositoryCustom{

    Optional<Friendship> findFriendshipByPublicId(UUID publicId);

}
