package hexa.chat.application.friendship.provided;

import hexa.chat.application.friendship.dto.FriendshipInfoResponse;
import hexa.chat.domain.friendship.Friendship;

import java.util.UUID;

public interface FriendshipFinder {

    Friendship findByPublicId(UUID publicId);

}
