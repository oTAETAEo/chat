package hexa.chat.adapter.jpa.friendship;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hexa.chat.domain.friendship.Friendship;
import hexa.chat.domain.friendship.QFriendship;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class FriendshipJapRepositoryImpl implements FriendshipRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    private final QFriendship qFriendship = QFriendship.friendship;

    @Override
    public Optional<Friendship> findFriendshipRequest(Long fromMemberId, Long toMemberId) {

        Friendship friendship = jpaQueryFactory.select(qFriendship)
            .from(qFriendship)
            .where(
                qFriendship.fromMember.id.eq(fromMemberId).and(qFriendship.toMember.id.eq(toMemberId))
                    .or(qFriendship.toMember.id.eq(toMemberId).and(qFriendship.fromMember.id.eq(fromMemberId)))
            ).fetchOne();

        return Optional.ofNullable(friendship);
    }

}
