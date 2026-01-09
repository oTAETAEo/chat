package hexa.chat.application.friendship.dto;

import hexa.chat.domain.friendship.Friendship;

public record FriendshipResponse(
    String publicId,
    String name,
    String nickname

) {
    public static FriendshipResponse ofFriend(Friendship friendship, Long memberId){

        if (friendship.getFromMember().getId().equals(memberId)){
            return ofReceiver(friendship);
        }

        return ofSender(friendship);
    }

    public static FriendshipResponse ofSender(Friendship friendship){
        return new FriendshipResponse(
            friendship.getPublicId().toString(),
            friendship.getFromMember().getName().name(),
            friendship.getFromMember().getNickname()
        );
    }

    public static FriendshipResponse ofReceiver(Friendship friendship){
        return new FriendshipResponse(
            friendship.getPublicId().toString(),
            friendship.getToMember().getName().name(),
            friendship.getToMember().getNickname()
        );
    }
}
