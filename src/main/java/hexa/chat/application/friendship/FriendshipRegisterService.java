package hexa.chat.application.friendship;

import hexa.chat.application.friendship.provided.FriendshipRegister;
import hexa.chat.application.friendship.required.FriendshipRepository;
import hexa.chat.application.member.provided.MemberFinder;
import hexa.chat.domain.friendship.DuplicateFriendShipException;
import hexa.chat.domain.friendship.FriendRegisterRequest;
import hexa.chat.domain.friendship.Friendship;
import hexa.chat.domain.friendship.FriendshipStatus;
import hexa.chat.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class FriendshipRegisterService implements FriendshipRegister {

    private final MemberFinder memberFinder;
    private final FriendshipRepository friendshipRepository;

    @Override
    public Friendship register(FriendRegisterRequest request) {
        Member from = memberFinder.findByName(request.fromName());
        Member to = memberFinder.findByName(request.toName());

        validateFriendshipRequest(from, to);

        Friendship friendship = Friendship.register(from, to);

        return friendshipRepository.save(friendship);
    }

    private void validateFriendshipRequest(Member from, Member to) {

        Optional<Friendship> friendship = friendshipRepository.findFriendshipRequest(from.getId(), to.getId());

        if (friendship.isEmpty())
            return;

        if (friendship.get().getStatus() == FriendshipStatus.PENDING){
            throw new DuplicateFriendShipException("존재하는 친구 요청이 있습니다.");
        }

        if (friendship.get().getStatus() == FriendshipStatus.BLOCKED){
            throw new IllegalArgumentException("차단 되어있는 사용자 입니다.");
        }
    }

}
