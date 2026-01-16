package hexa.chat.application.auth.provided;

import hexa.chat.application.auth.dto.NameCheckResponse;
import hexa.chat.application.auth.dto.SignUpRequest;
import hexa.chat.application.auth.dto.SignUpResponse;
import hexa.chat.domain.shared.Name;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
        SignUpRequest signUpRequest = new SignUpRequest(
            "test@test.com",
            "_test_",
            "test",
            "test@1234",
            "2011-01-10"
        );

        // when
        SignUpResponse signUpResponse = signUpUseCase.signUp(signUpRequest);

        // then
        assertThat(signUpResponse.welcomeMessage()).isEqualTo("test 님 가입을 환영합니다.");
    }

    @DisplayName("이름이 존재 하면 중복체크시 true 를 반환한다.")
    @Test
    void checkName() {
        // given
        SignUpRequest signUpRequest = new SignUpRequest(
            "test@test.com",
            "_test_",
            "test",
            "test@1234",
            "2011-01-10"
        );

        signUpUseCase.signUp(signUpRequest);

        // when
        NameCheckResponse response = signUpUseCase.checkName("test");

        // then
        assertThat(response.result()).isTrue();
    }
}
