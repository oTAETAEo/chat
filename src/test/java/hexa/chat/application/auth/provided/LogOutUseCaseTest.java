package hexa.chat.application.auth.provided;

import hexa.chat.application.auth.dto.LoginRequest;
import hexa.chat.application.auth.dto.SignUpRequest;
import hexa.chat.application.member.provided.MemberFinder;
import hexa.chat.domain.member.Member;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class LogOutUseCaseTest {

    @Autowired
    private LogOutUseCase logOutUseCase;

    @Autowired
    private LoginUseCase loginUseCase;

    @Autowired
    private SignUpUseCase signUpUseCase;

    @Autowired
    private MemberFinder memberFinder;

    @Autowired
    private EntityManager entityManager;

    @DisplayName("로그아웃 성공한다.")
    @Test
    void logOut() {
        // given
        SignUpRequest signUpRequest = new SignUpRequest(
            "test@test.com",
            "_test_",
            "test",
            "test@1234",
            "2011-01-10"
        );

        signUpUseCase.signUp(signUpRequest);

        LoginRequest loginRequest = new LoginRequest("test@test.com", "test@1234");

        String deviceId = UUID.randomUUID().toString();
        loginUseCase.login(loginRequest, deviceId);

        Member member = memberFinder.findByEmail(loginRequest.email());

        cleanJpaCache();

        // when - then
        logOutUseCase.logOut(member.getId(), deviceId);
        cleanJpaCache();

    }

    private void cleanJpaCache() {
        entityManager.flush();
        entityManager.clear();
    }

}