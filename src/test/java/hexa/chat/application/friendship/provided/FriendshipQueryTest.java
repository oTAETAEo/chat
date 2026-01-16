package hexa.chat.application.friendship.provided;

import hexa.chat.application.friendship.dto.FriendshipInfoResponse;
import hexa.chat.application.friendship.required.FriendshipRepository;
import hexa.chat.application.member.required.MemberRepository;
import hexa.chat.domain.friendship.Friendship;
import hexa.chat.domain.member.Member;
import hexa.chat.domain.member.MemberFixture;
import hexa.config.QueryTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class FriendshipQueryTest extends QueryTestBase {

    @Autowired
    private FriendshipQuery friendshipQuery;

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("사용자의 친구 관계 정보를 조회한다.")
    @Test
    void memberFriendshipInfo() {
        // given
        Member from = MemberFixture.createMember("han@han.com", "han", "_han_");
        Member to = MemberFixture.createMember("kim@kim.com", "kim", "_kim_");
        memberRepository.save(from);
        memberRepository.save(to);

        Friendship friendship = Friendship.register(from, to);
        friendship.accept();
        friendshipRepository.save(friendship);

        // when
        FriendshipInfoResponse result = friendshipQuery.memberFriendshipInfo(from.getId());

        // then
        assertThat(result.friendships()).hasSize(1);
    }

}