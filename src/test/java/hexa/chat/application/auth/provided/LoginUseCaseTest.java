package hexa.chat.application.auth.provided;

import hexa.chat.application.auth.dto.LoginRequest;
import hexa.chat.application.auth.dto.LoginResponse;
import hexa.chat.application.auth.required.RefreshTokenRepository;
import hexa.chat.application.member.required.MemberRepository;
import hexa.chat.domain.auth.InvalidCredentialsException;
import hexa.chat.domain.auth.refreshToken.RefreshToken;
import hexa.chat.domain.member.Member;
import hexa.chat.domain.member.MemberFixture;
import hexa.chat.domain.member.PasswordEncoder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class LoginUseCaseTest {

    @Autowired
    private LoginUseCase loginUseCase;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

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

    @DisplayName("다른 기기로 이중 로그인을 한다.")
    @Test
    void loginAgainWithSameAccountOnDifferentDevice () {
        // given
        Member member = memberRepository.save(MemberFixture.createMember("test@test.com", "test1234", passwordEncoder));

        LoginRequest loginRequest1 = new LoginRequest("test@test.com", "test1234");
        LoginRequest loginRequest2 = new LoginRequest("test@test.com", "test1234");

        // when
        LoginResponse response1 = loginUseCase.login(loginRequest1, "123456789");
        LoginResponse response2 = loginUseCase.login(loginRequest2, null);
        LoginResponse response3 = loginUseCase.login(loginRequest1, "");

        // then
        entityManager.flush();
        entityManager.clear();

        List<RefreshToken> result = refreshTokenRepository.findAllByMemberId(member.getId());
        assertThat(result).hasSize(3)
            .extracting("deviceId")
            .containsExactlyInAnyOrder(response1.deviceId(), response2.deviceId(), response3.deviceId());
    }

    @DisplayName("비밀번호 불일치로 로그인 실패 한다.")
    @Test
    void loginFail1 () {
        // given
        memberRepository.save(MemberFixture.createMember("test@test.com", "test1234", passwordEncoder));

        LoginRequest loginRequest = new LoginRequest("test@test.com", "no match password");

        // when - then
        assertThatThrownBy(() -> loginUseCase.login(loginRequest, null))
            .isInstanceOf(InvalidCredentialsException.class);
    }

    @DisplayName("회원이 존재하지 않아 로그인 실패 한다.")
    @Test
    void loginFail2 () {
        // given
        LoginRequest loginRequest = new LoginRequest("test@test.com", "test1234");

        // when - then
        assertThatThrownBy(() -> loginUseCase.login(loginRequest, null))
            .isInstanceOf(NoSuchElementException.class);
    }

}
