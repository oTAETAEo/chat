package hexa.chat.domain.auth.refreshToken;

import hexa.chat.domain.shared.Token;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class RefreshTokenTest {

    @DisplayName("Refresh Token 객체를 생성한다.")
    @Test
    void register() {
        // given
        Long memberId = 1L;
        String deviceId = "ipad1234";
        String tokenValue = "123.456.789";
        Date now = new Date();

        // when
        RefreshToken refreshToken = RefreshToken.register(memberId, deviceId, new Token(tokenValue, now));

        // then
        assertThat(refreshToken.getExpiryDate()).isEqualTo(getLocalDateTime(now.getTime()));
    }

    private static LocalDateTime getLocalDateTime(long validityMs) {
        Instant expiryInstant = Instant.ofEpochMilli(validityMs);
        return LocalDateTime.ofInstant(expiryInstant, ZoneOffset.UTC);
    }

}