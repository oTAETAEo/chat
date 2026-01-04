package hexa.chat.application.auth.dto;

import hexa.chat.domain.member.Member;
import hexa.chat.domain.shared.Email;
import hexa.chat.domain.shared.Name;

import java.time.LocalDate;
import java.util.UUID;

public record LoginResponse(

    String accessToken,
    String refreshToken,
    String deviceId

) {
    public static LoginResponse of(String accessToken, String refreshToken, String deviceId) {
        return new LoginResponse(
            accessToken,
            refreshToken,
            deviceId
        );
    }

}
