package hexa.chat.application.member.provided;

import hexa.chat.application.member.required.MemberRepository;
import hexa.chat.domain.member.Member;
import hexa.chat.domain.member.MemberFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class MemberFinderTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberFinder memberFinder;

    @DisplayName("회원 이름으로 사용자를 조회한다.")
    @Test
    void find() {
        // given
        Member member = MemberFixture.createMember("test@test.com", "test", "_test_");
        memberRepository.save(member);

        // when
        Member findMember = memberFinder.find("test");

        // then
        assertThat(member.getName()).isEqualTo(findMember.getName());
    }

}