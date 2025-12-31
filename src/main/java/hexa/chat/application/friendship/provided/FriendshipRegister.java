package hexa.chat.application.friendship.provided;

import hexa.chat.domain.friendship.FriendRegisterRequest;
import hexa.chat.domain.friendship.Friendship;

import jakarta.validation.Valid;

public interface FriendshipRegister {
    Friendship register(@Valid FriendRegisterRequest request);
}
