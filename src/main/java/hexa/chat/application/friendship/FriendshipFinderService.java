package hexa.chat.application.friendship;

import hexa.chat.application.friendship.dto.FriendshipInfoResponse;
import hexa.chat.application.friendship.dto.FriendshipResponse;
import hexa.chat.application.friendship.provided.FriendshipFinder;
import hexa.chat.application.friendship.required.FriendshipRepository;
import hexa.chat.domain.friendship.Friendship;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Validated
@Transactional
public class FriendshipFinderService implements FriendshipFinder {

    private final FriendshipRepository friendshipRepository;

    @Override
    public Friendship findByPublicId(UUID publicId) {
        return friendshipRepository.findFriendshipByPublicId(publicId)
            .orElseThrow(() -> new NoSuchElementException("친구추가 정보를 찾을 수 없습니다."));
    }

}
