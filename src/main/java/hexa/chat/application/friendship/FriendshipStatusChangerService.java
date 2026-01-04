package hexa.chat.application.friendship;

import hexa.chat.application.friendship.provided.FriendshipFinder;
import hexa.chat.application.friendship.provided.FriendshipStatusChanger;
import hexa.chat.application.friendship.required.FriendshipRepository;
import hexa.chat.application.member.provided.MemberFinder;
import hexa.chat.domain.friendship.Friendship;
import hexa.chat.domain.friendship.FriendshipAccessDeniedException;
import hexa.chat.domain.friendship.FriendshipStatusChangeRequest;
import hexa.chat.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class FriendshipStatusChangerService implements FriendshipStatusChanger {

    private final FriendshipRepository friendshipRepository;
    private final FriendshipFinder friendshipFinder;
    private final MemberFinder memberFinder;

    @Override
    public Friendship accept(Long currentMemberId, FriendshipStatusChangeRequest request) {

        Friendship friendship = friendshipFinder.find(request.friendshipPublicId());
        Member member = memberFinder.findById(currentMemberId);

        validateFriendshipAccess(friendship, member);

        friendship.accept();

        return friendshipRepository.save(friendship);
    }

    @Override
    public Friendship block(Long currentMemberId, FriendshipStatusChangeRequest request) {

        Friendship friendship = friendshipFinder.find(request.friendshipPublicId());
        Member member = memberFinder.findById(currentMemberId);

        validateFriendshipAccess(friendship, member);

        friendship.block();

        return friendshipRepository.save(friendship);
    }

    @Override
    public void reject(Long currentMemberId, FriendshipStatusChangeRequest request) {

        Friendship friendship = friendshipFinder.find(request.friendshipPublicId());
        Member member = memberFinder.findById(currentMemberId);

        validateFriendshipAccess(friendship, member);

        friendshipRepository.delete(friendship);
    }

    private void validateFriendshipAccess(Friendship friendship, Member member) {
        if (!Objects.equals(friendship.getFromMember().getId(), member.getId())
            && !Objects.equals(friendship.getToMember().getId(), member.getId())){
            throw new FriendshipAccessDeniedException("접근 할 수 없는 친구 요청입니다.");
        }
    }

}
