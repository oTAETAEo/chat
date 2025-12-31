package hexa.chat.application.friendship.provided;

import hexa.chat.domain.friendship.Friendship;

import java.util.UUID;

public interface FriendshipFinder {

    Friendship find(UUID publicId);
}
