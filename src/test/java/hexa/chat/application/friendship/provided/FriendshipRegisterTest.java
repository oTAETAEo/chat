package hexa.chat.application.friendship.provided;

import hexa.chat.application.member.required.MemberRepository;
import hexa.chat.domain.friendship.DuplicateFriendShipException;
import hexa.chat.domain.friendship.FriendRegisterRequest;
import hexa.chat.domain.friendship.Friendship;
import hexa.chat.domain.member.Member;
import hexa.chat.domain.member.MemberFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class FriendshipRegisterTest {

    @Autowired
    private FriendshipRegister friendshipRegister;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("친구 요청을 저장한다.")
    @Test
    void register() {
        // given
        Member from = memberRepository.save(MemberFixture.createMember("han@han.com", "han", "_han_"));
        Member to = memberRepository.save(MemberFixture.createMember("kim@kim.com", "kim", "_kim_"));

        FriendRegisterRequest request = new FriendRegisterRequest(from.getName().name(), to.getName().name());

        // when
        Friendship friendship = friendshipRegister.register(request);

        // then
        assertThat(friendship.getFromMember().getName().name()).isEqualTo("han");
        assertThat(friendship.getToMember().getName().name()).isEqualTo("kim");
    }

    @DisplayName("친구 요청이 존재하는데 한번더 친구요청을 보내는경우 예외가 발생한다.")
    @Test
    void registerFail() {
        // given
        Member from = memberRepository.save(MemberFixture.createMember("han@han.com", "han", "_han_"));
        Member to = memberRepository.save(MemberFixture.createMember("kim@kim.com", "kim", "_kim_"));

        FriendRegisterRequest request = new FriendRegisterRequest(from.getName().name(), to.getName().name());
        friendshipRegister.register(request);

        // when - then
        assertThatThrownBy(() -> friendshipRegister.register(request))
            .isInstanceOf(DuplicateFriendShipException.class);
    }


}