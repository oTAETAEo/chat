package hexa.chat.domain.friendship;

import hexa.chat.domain.AbstractEntity;
import hexa.chat.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friendship extends AbstractEntity {

    @NaturalId
    @Column(unique = true, nullable = false)
    private UUID publicId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_member_id", nullable = false)
    private Member fromMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_member_id", nullable = false)
    private Member toMember;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private FriendshipStatus status;

    public static Friendship register(Member fromMember, Member toMember){
        Friendship friendship = new Friendship();

        friendship.fromMember = fromMember;
        friendship.toMember = toMember;
        friendship.status = FriendshipStatus.PENDING;
        friendship.publicId = UUID.randomUUID();

        return friendship;
    }

    public void accept(){
        if (status != FriendshipStatus.PENDING) throw new IllegalArgumentException("대기중인 친구 추가 요청이 있습니다.");

        status = FriendshipStatus.ACCEPTED;
    }

    public void block(){
        if (status != FriendshipStatus.ACCEPTED) throw new IllegalArgumentException("승인되지 않은 친구는 차단할 수 없습니다.");

        status = FriendshipStatus.BLOCKED;
    }

}
