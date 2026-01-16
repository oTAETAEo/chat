package hexa.chat.application.member.provided;

import hexa.chat.application.member.dto.MemberInfoPublicResponse;
import hexa.chat.application.member.required.MemberRepository;
import hexa.chat.domain.member.Member;
import hexa.chat.domain.member.MemberFixture;
import hexa.config.QueryTestBase;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class MemberQueryTest extends QueryTestBase {

    @Autowired
    private MemberQuery memberQuery;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("회원의 공개 정보를 조회한다.")
    @Test
    void memberPublicInfo() {
        // given
        Member member = MemberFixture.createMember();
        memberRepository.save(member);

        cleanJpaCache();

        // when
        MemberInfoPublicResponse result = memberQuery.memberPublicInfo(member.getId());

        // then
        assertThat(result.publicId()).isEqualTo(member.getPublicId());
    }


}
