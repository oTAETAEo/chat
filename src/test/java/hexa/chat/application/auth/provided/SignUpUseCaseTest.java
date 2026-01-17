package hexa.chat.application.auth.provided;

import hexa.chat.application.auth.dto.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class SignUpUseCaseTest {

    @Autowired
    private SignUpUseCase signUpUseCase;

    @DisplayName("회원가입을 한다.")
    @Test
    void signUp() {
        // given
        SignUpRequest signUpRequest = SignUpFixture.createSignUpRequest();

        // when
        SignUpResponse signUpResponse = signUpUseCase.signUp(signUpRequest);

        // then
        assertThat(signUpResponse.welcomeMessage()).isEqualTo("test 님 가입을 환영합니다.");
    }

    @DisplayName("이름이 존재 하면 false 를 반환한다.")
    @Test
    void checkName() {
        // given
        SignUpRequest signUpRequest = SignUpFixture.createSignUpRequest();

        signUpUseCase.signUp(signUpRequest);

        // when
        NameCheckResponse response = signUpUseCase.checkName(new NameCheckRequest("test"));

        // then
        assertThat(response.result()).isFalse();
    }

    @DisplayName("이메일이 존재 하면 false 를 반환한다.")
    @Test
    void checkEmail() {
        // given
        SignUpRequest signUpRequest = SignUpFixture.createSignUpRequest();

        signUpUseCase.signUp(signUpRequest);

        // when
        EmailCheckResponse response = signUpUseCase.checkEmail(new EmailCheckRequest("test@test.com"));

        // then
        assertThat(response.result()).isFalse();
    }


}
