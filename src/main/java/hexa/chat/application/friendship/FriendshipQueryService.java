package hexa.chat.application.friendship;

import hexa.chat.application.friendship.dto.FriendshipInfoResponse;
import hexa.chat.application.friendship.dto.FriendshipResponse;
import hexa.chat.application.friendship.provided.FriendshipQuery;
import hexa.chat.application.friendship.required.FriendshipRepository;
import hexa.chat.domain.friendship.Friendship;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FriendshipQueryService implements FriendshipQuery {

    private final FriendshipRepository friendshipRepository;

    @Override
    public FriendshipInfoResponse memberFriendshipInfo(Long memberId) {
        List<Friendship> acceptedFriendships = friendshipRepository.findAllAcceptedFriendshipsByMemberId(memberId);
        List<Friendship> senderPendingFriendships = friendshipRepository.findAllPendingFriendshipsBySenderId(memberId);
        List<Friendship> receiverPendingFriendships = friendshipRepository.findAllPendingFriendshipsByReceiverId(memberId);

        List<FriendshipResponse> friendInfo = acceptedFriendships.stream()
            .map(friendship -> FriendshipResponse.ofFriend(friendship, memberId))
            .toList();

        List<FriendshipResponse> senderInfo = senderPendingFriendships.stream()
            .map(FriendshipResponse::ofSender)
            .toList();

        List<FriendshipResponse> receiverInfo = receiverPendingFriendships.stream()
            .map(FriendshipResponse::ofReceiver)
            .toList();

        return new FriendshipInfoResponse(
            friendInfo, senderInfo, receiverInfo);
    }
}
