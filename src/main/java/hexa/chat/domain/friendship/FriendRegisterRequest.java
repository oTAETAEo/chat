package hexa.chat.domain.friendship;

import jakarta.validation.constraints.Size;

public record FriendRegisterRequest(

    @Size(min = 1, max = 20)
    String fromName,

    @Size(min = 1, max = 20)
    String toName
)
{}
