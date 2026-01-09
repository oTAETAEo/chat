package hexa.chat.application.member.provided;

import hexa.chat.domain.member.*;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class MemberRegisterTest {

    @Autowired
    MemberRegister memberRegister;

    @DisplayName("회원을 저장 한다.")
    @Test
    void register() {
        // given
        MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest();

        // when
        Member member = memberRegister.register(request);

        // then
        assertThat(member.getName().name()).isEqualTo(request.name());
    }

    @DisplayName("회원 저장을 실패한다.")
    @Test
    void registerFail() {
        // given
        MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest("invalid email", "kim", "_kim_");

        // when- then
        assertThatThrownBy(() -> memberRegister.register(request))
            .isInstanceOf(ConstraintViolationException.class);
    }

    @DisplayName("이메일이 존재하면 예외가 발생한다.")
    @Test
    void duplicateEmailException() {
        // given
        MemberRegisterRequest request1 = MemberFixture.createMemberRegisterRequest("test@test.com", "kim", "_kim_");
        MemberRegisterRequest request2 = MemberFixture.createMemberRegisterRequest("test@test.com", "han", "_han_");

        memberRegister.register(request1);

        // when - then
        assertThatThrownBy(() -> memberRegister.register(request2))
            .isInstanceOf(DuplicateEmailException.class);
    }

    @DisplayName("이름이 존재하면 예외가 발생한다.")
    @Test
    void duplicateNameException() {
        // given
        MemberRegisterRequest request1 = MemberFixture.createMemberRegisterRequest("kim1@test.com", "kim", "_kim_");
        MemberRegisterRequest request2 = MemberFixture.createMemberRegisterRequest("kim2@test.com", "kim", "_kim_");

        // when
        memberRegister.register(request1);

        // then
        assertThatThrownBy(() -> memberRegister.register(request2))
            .isInstanceOf(DuplicateNameException.class);
    }

}