package hexa.chat.adapter.security;

import hexa.chat.domain.member.MemberRole;
import hexa.chat.domain.shared.Token;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class JwtTokenProviderTest {

    private JwtTokenProvider tokenProvider;

    @BeforeEach
     void setUp() {
        String secret = "01234567890123456789012345678901";
        long accessMs = 1_800_000L;     // 30분
        long refreshMs = 1_209_600_000L; // 14일

        tokenProvider = new JwtTokenProvider(secret, accessMs, refreshMs);
    }

    @DisplayName("Access Token을 생성한다.")
    @Test
    void createAccessToken() {
        // given
        UUID publicId = UUID.randomUUID();
        MemberRole role = MemberRole.USER;
        long now = floorToSecond(new Date().getTime());

        // when
        Token token = tokenProvider.createAccessToken(publicId, role, now);

        // then
        var claims = Jwts.parser()
            .verifyWith(getKeyViaReflection())
            .build()
            .parseSignedClaims(token.value())
            .getPayload();

        assertThat(claims.getSubject()).isEqualTo(publicId.toString());
        assertThat(claims.get("role", String.class)).isEqualTo(role.name());
        assertThat(claims.getIssuedAt().getTime()).isEqualTo(now);
        assertThat(claims.getExpiration().getTime())
            .isEqualTo(now + 1_800_000L);
    }

    @DisplayName("Refresh Token을 생성한다.")
    @Test
    void createRefreshToken() {
        // given
        UUID publicId = UUID.randomUUID();
        long now = floorToSecond(new Date().getTime());

        // when
        Token token = tokenProvider.createRefreshToken(publicId, now);

        // then
        var claims = Jwts.parser()
            .verifyWith(getKeyViaReflection())
            .build()
            .parseSignedClaims(token.value())
            .getPayload();

        assertThat(claims.getSubject()).isEqualTo(publicId.toString());
        assertThat(claims.getIssuedAt().getTime()).isEqualTo(now);
        assertThat(claims.getExpiration().getTime())
            .isEqualTo(now + 1_209_600_000L);
    }

    @DisplayName("Access Token을 파싱하여 페이로드를 반환한다.")
    @Test
    void parseAccessToken_Success() {
        // given
        UUID publicId = UUID.randomUUID();
        MemberRole role = MemberRole.USER;
        long now = floorToSecond(new Date().getTime());
        Token token = tokenProvider.createAccessToken(publicId, role, now);

        // when
        var result = tokenProvider.parseAccessToken(token.value());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().publicId()).isEqualTo(publicId);
        assertThat(result.get().role()).isEqualTo(role);
    }

    @DisplayName("만료된 Access Token을 파싱하면 빈 값을 반환한다.")
    @Test
    void parseAccessToken_Expired() {
        // given
        UUID publicId = UUID.randomUUID();
        MemberRole role = MemberRole.USER;

        // 1시간 전으로 설정하여 시간 오차로 인한 테스트 실패 방지
        long expiredTime = System.currentTimeMillis() - (60 * 60 * 1000);

        Token token = tokenProvider.createAccessToken(publicId, role, expiredTime);

        // when
        var result = tokenProvider.parseAccessToken(token.value());

        // then
        assertThat(result).isNotPresent(); // isEmpty()와 동일하지만 Optional 전용 단언문 사용
    }

    @DisplayName("잘못된 Access Token을 파싱하면 빈 값을 반환한다.")
    @Test
    void parseAccessToken_InvalidToken() {
        // given
        String invalidToken = "invalid.token.value";

        // when
        var result = tokenProvider.parseAccessToken(invalidToken);

        // then
        assertThat(result).isEmpty();
    }

    @DisplayName("role이 없는 Access Token을 파싱하면 빈 값을 반환한다.")
    @Test
    void parseAccessToken_MissingRole() {
        // given
        UUID publicId = UUID.randomUUID();
        long now = floorToSecond(new Date().getTime());
        Date validity = new Date(now + 1_800_000L);

        String tokenWithoutRole = Jwts.builder()
            .subject(publicId.toString())
            .issuedAt(new Date(now))
            .expiration(validity)
            .signWith(getKeyViaReflection())
            .compact();

        // when
        var result = tokenProvider.parseAccessToken(tokenWithoutRole);

        // then
        assertThat(result).isEmpty();
    }

    private SecretKey getKeyViaReflection() {
        return Keys.hmacShaKeyFor("01234567890123456789012345678901"
            .getBytes(StandardCharsets.UTF_8));
    }

    static long floorToSecond(long epochMilli) {
        return epochMilli / 1000 * 1000;
    }

}
