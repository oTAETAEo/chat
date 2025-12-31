package hexa.chat.application.friendship.required;

import hexa.chat.application.member.required.MemberRepository;
import hexa.chat.domain.friendship.Friendship;
import hexa.chat.domain.member.Member;
import hexa.chat.domain.member.MemberFixture;
import hexa.config.TestQueryDslConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestQueryDslConfig.class)
class FriendshipRepositoryTest {

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("친구 관계 객체를 저장한다.")
    @Test
    void save() {
        // given
        Member from = MemberFixture.createMember("han@han.com", "han", "_han_");
        Member to = MemberFixture.createMember("kim@kim.com", "kim", "_kim_");
        memberRepository.save(from);
        memberRepository.save(to);

        Friendship friendship = Friendship.register(from, to);

        // when
        Friendship result = friendshipRepository.save(friendship);

        // then
        assertThat(friendship.getId()).isEqualTo(result.getId());
    }

    @DisplayName("친구 요청을 저장하면 from과 to로 조회할 수 있다.")
    @Test
    void findFriendshipRequest() {
        // given
        Member from = MemberFixture.createMember("han@han.com", "han", "_han_");
        Member to = MemberFixture.createMember("kim@kim.com", "kim", "_kim_");

        memberRepository.save(from);
        memberRepository.save(to);

        Friendship friendship = Friendship.register(from, to);
        friendshipRepository.save(friendship);

        // when
        Optional<Friendship> result = friendshipRepository.findFriendshipRequest(from.getId(), to.getId());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getFromMember()).isEqualTo(from);
        assertThat(result.get().getToMember()).isEqualTo(to);
    }

    @DisplayName("친구 관계 객체를 publicId로 조회한다")
    @Test
    void findByFromMemberPublicId() {
        // given
        Member from = MemberFixture.createMember("han@han.com", "han", "_han_");
        Member to = MemberFixture.createMember("kim@kim.com", "kim", "_kim_");

        memberRepository.save(from);
        memberRepository.save(to);

        Friendship friendship = Friendship.register(from, to);
        friendshipRepository.save(friendship);

        // when
        Optional<Friendship> result = friendshipRepository.findFriendshipByPublicId(friendship.getPublicId());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getFromMember()).isEqualTo(from);
        assertThat(result.get().getToMember()).isEqualTo(to);
    }
}