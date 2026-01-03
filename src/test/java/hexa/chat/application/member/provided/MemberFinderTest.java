package hexa.chat.application.member.provided;

import hexa.chat.application.member.required.MemberRepository;
import hexa.chat.domain.member.Member;
import hexa.chat.domain.member.MemberFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class MemberFinderTest {

    @Autowired
    private MemberFinder memberFinder;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("회원 이름으로 사용자를 조회한다.")
    @Test
    void find1() {
        // given
        Member member = MemberFixture.createMember("test@test.com", "test", "_test_");
        memberRepository.save(member);

        // when
        Member findMember = memberFinder.find("test");

        // then
        assertThat(member.getName()).isEqualTo(findMember.getName());
    }

    @DisplayName("회원 이름으로 사용자를 조회한다.")
    @Test
    void find2() {
        // given
        Member member = MemberFixture.createMember();
        Member result = memberRepository.save(member);

        // when
        Member findMember = memberFinder.find(result.getId());

        // then
        assertThat(member.getName()).isEqualTo(findMember.getName());
    }

    @DisplayName("회원 아이디로 사용자를 모두 조회한다.")
    @Test
    void findAll() {
        // given
        Member member1 = MemberFixture.createMember("han@han.com", "han", "_han_");
        Member member2 = MemberFixture.createMember("kim@kim.com", "kim", "_kim_");
        List<Member> members = memberRepository.saveAll(List.of(member1, member2));

        // when
        List<Member> result = memberFinder.findAll(List.of(member1.getId(), member2.getId()));

        // then
        assertThat(result).hasSize(2)
            .containsExactlyInAnyOrder(member1, member2);
    }

}