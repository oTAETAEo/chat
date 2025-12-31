package hexa.chat.domain.friendship;

import hexa.chat.domain.member.Member;
import hexa.chat.domain.member.MemberFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FriendshipTest {

    @DisplayName("친구 요청 객체를 생성한다.")
    @Test
    void register() {
        // given
        Member from = MemberFixture.createMember(1L);
        Member to = MemberFixture.createMember(2L);

        // when
        Friendship register = Friendship.register(from, to);

        // then
        assertThat(register.getFromMember().getId()).isEqualTo(from.getId());
        assertThat(register.getToMember().getId()).isEqualTo(to.getId());
        assertThat(register.getStatus()).isEqualTo(FriendshipStatus.PENDING);
    }

    @DisplayName("친구 요청 객체 상태를 ACCEPTED로 변경한다.")
    @Test
    void changeStatus1() {
        Member from = MemberFixture.createMember(1L);
        Member to = MemberFixture.createMember(2L);

        // when
        Friendship register = Friendship.register(from, to);

        // then
        register.accept();
        assertThat(register.getStatus()).isEqualTo(FriendshipStatus.ACCEPTED);
    }

    @DisplayName("친구 요청 객체 상태를 BLOCKED로 변경한다.")
    @Test
    void changeStatus2() {
        Member from = MemberFixture.createMember(1L);
        Member to = MemberFixture.createMember(2L);

        // when
        Friendship register = Friendship.register(from, to);
        register.accept();

        // then
        register.block();
        assertThat(register.getStatus()).isEqualTo(FriendshipStatus.BLOCKED);
    }

    @DisplayName("친구 요청 객체 상태가 PENDING이 아닐때 ACCEPTED 변경시 예외가 발생한다.")
    @Test
    void chengStatusFail1() {
        Member from = MemberFixture.createMember(1L);
        Member to = MemberFixture.createMember(2L);

        // when
        Friendship register = Friendship.register(from, to);
        register.accept();

        // then
        assertThatThrownBy(() -> register.accept())
            .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("친구 요청 객체 상태가 ACCEPTED이 아닐때 BLOCKED 변경시 예외가 발생한다.")
    @Test
    void chengStatusFail2() {
        Member from = MemberFixture.createMember(1L);
        Member to = MemberFixture.createMember(2L);

        // when
        Friendship register = Friendship.register(from, to);

        // then
        assertThatThrownBy(() -> register.block())
            .isInstanceOf(IllegalArgumentException.class);

    }

}