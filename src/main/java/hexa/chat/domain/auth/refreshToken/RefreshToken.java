package hexa.chat.domain.auth.refreshToken;

import hexa.chat.domain.AbstractEntity;
import hexa.chat.domain.shared.Token;
import hexa.snowflake.SnowflakeId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "refresh_tokens", indexes = {
    @Index(name = "idx_refresh_token_member_device", columnList = "memberId, deviceId", unique = true)
})
public class RefreshToken {

    @Id
    @SnowflakeId
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private String deviceId;

    @Column(nullable = false, length = 512)
    private String tokenValue;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    public static RefreshToken register(Long memberId, String deviceId, Token token) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.memberId = memberId;
        refreshToken.deviceId = deviceId;
        refreshToken.tokenValue = token.value();
        refreshToken.expiryDate = getLocalDateTime(token.validity().getTime());

        return refreshToken;
    }

    public boolean isExpired(LocalDateTime now) {
        return expiryDate.isBefore(now);
    }

    public RefreshToken rotate(Token refreshToken) {
        this.tokenValue = refreshToken.value();
        this.expiryDate = getLocalDateTime(refreshToken.validity().getTime());
        return this;
    }

    private static LocalDateTime getLocalDateTime(long validityMs) {
        Instant expiryInstant = Instant.ofEpochMilli(validityMs);
        return LocalDateTime.ofInstant(expiryInstant, ZoneOffset.UTC);
    }

}
