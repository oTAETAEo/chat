package hexa.chat.adapter.web.dto;

import hexa.chat.application.auth.dto.LoginResponse;

public record LoginInfoResponse(

    String accessToken,
    String refreshToken,
    String grantType

) {

    public static LoginInfoResponse of(LoginResponse response){
        return new LoginInfoResponse(
            response.accessToken(),
            response.refreshToken(),
            "Bearer "
        );
    }

}
