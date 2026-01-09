package hexa.chat.adapter.jpa.friendship;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hexa.chat.domain.friendship.Friendship;
import hexa.chat.domain.friendship.FriendshipStatus;
import hexa.chat.domain.friendship.QFriendship;
import lombok.RequiredArgsConstructor;

import java.util.List;
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

    @Override
    public List<Friendship> findAllAcceptedFriendshipsByMemberId(Long memberId) {
        return jpaQueryFactory
            .selectFrom(qFriendship)
            .join(qFriendship.fromMember).fetchJoin()
            .join(qFriendship.toMember).fetchJoin()
            .where(
                qFriendship.status.eq(FriendshipStatus.ACCEPTED)
                    .and(
                        qFriendship.fromMember.id.eq(memberId).or(qFriendship.toMember.id.eq(memberId))
                    )
            )
            .fetch();
    }

    @Override
    public List<Friendship> findAllPendingFriendshipsBySenderId(Long memberId) {
        return jpaQueryFactory
            .selectFrom(qFriendship)
            .join(qFriendship.toMember).fetchJoin()
            .where(
                qFriendship.status.eq(FriendshipStatus.PENDING).and(qFriendship.fromMember.id.eq(memberId))
            )
            .fetch();
    }

    @Override
    public List<Friendship> findAllPendingFriendshipsByReceiverId(Long memberId) {
        return jpaQueryFactory
            .selectFrom(qFriendship)
            .join(qFriendship.fromMember).fetchJoin()
            .where(
                qFriendship.status.eq(FriendshipStatus.PENDING).and(qFriendship.toMember.id.eq(memberId))
            )
            .fetch();
    }

}
