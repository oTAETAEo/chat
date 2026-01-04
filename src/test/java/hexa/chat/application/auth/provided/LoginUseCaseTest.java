package hexa.chat.application.auth.provided;

import hexa.chat.application.auth.dto.LoginRequest;
import hexa.chat.application.auth.dto.LoginResponse;
import hexa.chat.application.auth.required.TokenProvider;
import hexa.chat.application.member.required.MemberRepository;
import hexa.chat.domain.auth.InvalidCredentialsException;
import hexa.chat.domain.member.Member;
import hexa.chat.domain.member.MemberFixture;
import hexa.chat.domain.member.PasswordEncoder;
import hexa.chat.domain.shared.Token;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class LoginUseCaseTest {

    @Autowired
    private LoginUseCase loginUseCase;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EntityManager entityManager;

    @Value("${jwt.secret}")
    private String secret;

    @DisplayName("로그인을 한다.")
    @Test
    void login () {
        // given
        Member member = memberRepository.save(MemberFixture.createMember("test@test.com", "test1234", passwordEncoder));

        LoginRequest loginRequest = new LoginRequest("test@test.com", "test1234");

        // when
        LoginResponse result = loginUseCase.login(loginRequest, null);

        // then
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        entityManager.flush();
        entityManager.clear();

        var claims = Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(result.refreshToken())
            .getPayload();

        assertThat(member.getPublicId().toString()).isEqualTo(claims.getSubject());
    }

    @DisplayName("로그인 실패 한다.")
    @Test
    void loginFail () {
        // given
        memberRepository.save(MemberFixture.createMember("test@test.com", "test1234", passwordEncoder));

        LoginRequest loginRequest = new LoginRequest("test@test.com", "no match password");

        // when - then
        assertThatThrownBy(() -> loginUseCase.login(loginRequest, null))
            .isInstanceOf(InvalidCredentialsException.class);
    }

}
