package hexa.chat.application.auth.dto;

public record LoginResponse(

        String accessToken,
        String refreshToken,
        String deviceId

) {
    public static LoginResponse of(String accessToken, String refreshToken, String deviceId) {
        return new LoginResponse(
                accessToken,
                refreshToken,
                deviceId);
    }

}
