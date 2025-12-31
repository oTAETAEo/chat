package hexa.chat.domain.member;

import hexa.chat.domain.shared.Name;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberTest {

    @DisplayName("회원 객체를 생성한다")
    @Test
    void register1() {
        // given
        PasswordEncoder passwordEncoder = MemberFixture.createPasswordEncoder();

        MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest();

        // when
        Member member = Member.register(request, passwordEncoder);

        // then
        assertThat(member.getEmail().address()).isEqualTo(request.email());
        assertThat(member.getName().name()).isEqualTo(request.name());
    }

    @DisplayName("회원의 별명이 없는 경우 회원의 이름으로 변경해 회원 객체를 생성한다.")
    @Test
    void register2() {
        // given
        PasswordEncoder passwordEncoder = MemberFixture.createPasswordEncoder();

        MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest("test@tests.com", "test", null);

        // when
        Member member = Member.register(request, passwordEncoder);

        // then
        assertThat(member.getName().name()).isEqualTo(member.getNickname());
    }

    @DisplayName("회원 객체를 생성할때 이메일 형식이 맞지 않으면 예외가 발생한다.")
    @Test
    void registerFile1() {
        // given
        PasswordEncoder passwordEncoder = MemberFixture.createPasswordEncoder();

        MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest("invalid email", "test", "otesto");

        // when - then
        assertThatThrownBy(() -> Member.register(request, passwordEncoder))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("회원 객체를 생성할때 이름 형식이 맞지 않으면 예외가 발생한다.")
    @Test
    void registerFile2() {
        // given
        PasswordEncoder passwordEncoder = MemberFixture.createPasswordEncoder();

        MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest("test@test.com", "invalid name", "otesto");

        // when - then
        assertThatThrownBy(() -> Member.register(request, passwordEncoder))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("회원 객체의 비밀번호를 검증 한다.")
    @Test
    void verifyPassword() {
        // given
        Member member = MemberFixture.createMember();
        PasswordEncoder passwordEncoder = MemberFixture.createPasswordEncoder();

        // when - then
        assertThat(member.verifyPassword("newPassword", passwordEncoder)).isFalse();
        assertThat(member.verifyPassword("test1234", passwordEncoder)).isTrue();
    }

    @DisplayName("회원 객체의 이름을 변경한다.")
    @Test
    void changeName() {
        // given
        Member member = MemberFixture.createMember();

        // when
        member.changeName("new_name");

        // then
        assertThat(member.getName().name()).isEqualTo("new_name");
    }

    @DisplayName("회원 객체의 별명을 변경한다.")
    @Test
    void changeNickname() {
        // given
        Member member = MemberFixture.createMember();

        // when
        member.changeNickname("new_nickname");

        // then
        assertThat(member.getNickname()).isEqualTo("new_nickname");
    }

    @DisplayName("회원 객체의 생일을 변경한다.")
    @Test
    void changeBirthDate() {
        // given
        LocalDate newBirthDate = LocalDate.of(2025, 2, 2);

        Member member = MemberFixture.createMember();

        // when
        member.changeBirthDate(newBirthDate);

        // then
        assertThat(member.getBirthDate()).isEqualTo(newBirthDate);
    }

    @DisplayName("회원 객체의 비밀번호를 변경한다.")
    @Test
    void changePassword() {
        // given
        Member member = MemberFixture.createMember();
        PasswordEncoder passwordEncoder = MemberFixture.createPasswordEncoder();
        String newPassword = "newPassword";

        // when
        member.changePassword(newPassword, passwordEncoder);

        // then
        passwordEncoder.matches(newPassword, member.getPasswordHash());
    }

}
