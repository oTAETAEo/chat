package hexa.chat.application.member.required;

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
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("회원 객체를 저장한다.")
    @Test
    void save() {
        // given
        Member member = MemberFixture.createMember();

        // when
        Member result = memberRepository.save(member);

        // then
        assertThat(member.getName().name()).isEqualTo(result.getName().name());
    }

    @DisplayName("이메일로 회원 객체를 조회한다.")
    @Test
    void findByEmail() {
        // given
        Member member = MemberFixture.createMember();
        memberRepository.save(member);

        // when
        Optional<Member> result = memberRepository.findByEmail(member.getEmail());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo(member.getEmail());
    }

    @DisplayName("이름으로 회원 객체를 조회한다.")
    @Test
    void findByName() {
        // given
        Member member = MemberFixture.createMember();
        memberRepository.save(member);

        // when
        Optional<Member> result = memberRepository.findByName(member.getName());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo(member.getName());
    }

    @DisplayName("PK로 회원 객체를 조회한다.")
    @Test
    void findById() {
        // given
        Member member = MemberFixture.createMember();
        memberRepository.save(member);

        // when
        Optional<Member> result = memberRepository.findById(member.getId());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(member.getId());
    }

}