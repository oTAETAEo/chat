package hexa.chat.adapter.security;

import hexa.chat.domain.shared.Token;
import hexa.chat.application.auth.required.TokenProvider;
import hexa.chat.domain.member.MemberRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Component
public class JwtTokenProvider implements TokenProvider {

    private final SecretKey key;

    private final long accessTokenValidityMillis;
    private final long refreshTokenValidityMillis;

    public JwtTokenProvider(
        @Value("${jwt.secret}") String secret,
        @Value("${jwt.access-token-validity-ms}") long accessTokenValidityMillis,
        @Value("${jwt.refresh-token-validity-ms}") long refreshTokenValidityMillis
    ) {
        try {
            this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
            this.accessTokenValidityMillis = accessTokenValidityMillis;
            this.refreshTokenValidityMillis = refreshTokenValidityMillis;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to initialize JwtTokenProvider", e);
        }
    }

    @Override
    public Token createAccessToken(UUID publicId, MemberRole role, long now) {
        Date validity = new Date(now + accessTokenValidityMillis);

        String accessToken = Jwts.builder()
            .subject(publicId.toString())
            .claim("role", role.name())
            .issuedAt(new Date(now))
            .expiration(validity)
            .signWith(key)
            .compact();

        return new Token(accessToken, validity);
    }

    @Override
    public Token createRefreshToken(UUID publicId, long now) {
        Date validity = new Date(now + refreshTokenValidityMillis);

        String refreshToken = Jwts.builder()
            .subject(publicId.toString())
            .issuedAt(new Date(now))
            .expiration(validity)
            .signWith(key)
            .compact();

        return new Token(refreshToken, validity);
    }

    @Override
    public Optional<AccessTokenPayload> parseAccessToken(String token) {
        try {
            Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

            String subject = claims.getSubject();
            String roleName = claims.get("role", String.class);

            if (subject == null || roleName == null) {
                return Optional.empty();
            }

            return Optional.of(new AccessTokenPayload(
                UUID.fromString(subject),
                MemberRole.valueOf(roleName)
            ));
        } catch (JwtException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
