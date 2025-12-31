package hexa.chat.domain.friendship;

import java.util.UUID;

public record FriendshipStatusChangeRequest(
    UUID friendshipPublicId,
    FriendshipAction action
)
{}
