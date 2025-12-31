package hexa.chat.application.friendship.provided;

import hexa.chat.application.friendship.required.FriendshipRepository;
import hexa.chat.application.member.required.MemberRepository;
import hexa.chat.domain.friendship.*;
import hexa.chat.domain.member.Member;
import hexa.chat.domain.member.MemberFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FriendshipStatusChangerTest {

    @Autowired
    private FriendshipStatusChanger friendshipStatusChanger;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FriendshipRepository friendshipRepository;

    @DisplayName("친구 요청을 승인한다.")
    @Test
    void accept() {
        // given
        Member from = memberRepository.save(MemberFixture.createMember("han@han.com", "han", "_han_"));
        Member to = memberRepository.save(MemberFixture.createMember("kim@kim.com", "kim", "_kim_"));

        Friendship friendship = friendshipRepository.save(Friendship.register(from, to));

        FriendshipStatusChangeRequest request = new FriendshipStatusChangeRequest(friendship.getPublicId(), FriendshipAction.ACCEPT);

        // when
        Friendship result = friendshipStatusChanger.accept(from.getId(), request);

        // then
        assertThat(result.getStatus()).isEqualTo(FriendshipStatus.ACCEPTED);
    }

    @DisplayName("친구 요청을 거절한다.")

    @Test
    void reject() {
        // given
        Member from = memberRepository.save(MemberFixture.createMember("han@han.com", "han", "_han_"));
        Member to = memberRepository.save(MemberFixture.createMember("kim@kim.com", "kim", "_kim_"));

        Friendship friendship = friendshipRepository.save(Friendship.register(from, to));

        FriendshipStatusChangeRequest request = new FriendshipStatusChangeRequest(friendship.getPublicId(), FriendshipAction.ACCEPT);

        Optional<Friendship> check = friendshipRepository.findFriendshipByPublicId(friendship.getPublicId());
        assertThat(check).isPresent();

        // when
        friendshipStatusChanger.reject(from.getId(), request);

        // then
        Optional<Friendship> result = friendshipRepository.findFriendshipByPublicId(friendship.getPublicId());
        assertThat(result).isEmpty();
    }

    @DisplayName("친구를 차단 한다.")
    @Test
    void block() {
        // given
        Member from = memberRepository.save(MemberFixture.createMember("han@han.com", "han", "_han_"));
        Member to = memberRepository.save(MemberFixture.createMember("kim@kim.com", "kim", "_kim_"));

        Friendship friendship = friendshipRepository.save(Friendship.register(from, to));

        FriendshipStatusChangeRequest request = new FriendshipStatusChangeRequest(friendship.getPublicId(), FriendshipAction.ACCEPT);
        friendship.accept();

        // when
        Friendship result = friendshipStatusChanger.block(from.getId(), request);

        // then
        assertThat(result.getStatus()).isEqualTo(FriendshipStatus.BLOCKED);
    }

    @DisplayName("접근 할 수 없는 친구 요청 변경시 예외가 발생 한다.")
    @Test
    void changeStatusFail() {
        // given
        Member from = memberRepository.save(MemberFixture.createMember("han@han.com", "han", "_han_"));
        Member to = memberRepository.save(MemberFixture.createMember("kim@kim.com", "kim", "_kim_"));
        Member inaccessibleMember = memberRepository.save(MemberFixture.createMember("chai@chai.com", "chai", "_chai_"));

        Friendship friendship = friendshipRepository.save(Friendship.register(from, to));

        FriendshipStatusChangeRequest request = new FriendshipStatusChangeRequest(friendship.getPublicId(), FriendshipAction.ACCEPT);

        // when - then
        assertThatThrownBy(() -> friendshipStatusChanger.accept(inaccessibleMember.getId(), request))
            .isInstanceOf(FriendshipAccessDeniedException.class);
    }

}