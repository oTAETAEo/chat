package hexa.chat.adapter.web.auth;

import hexa.chat.adapter.web.TokenName;
import hexa.chat.application.auth.dto.LoginRequest;
import hexa.chat.application.auth.dto.LoginResponse;
import hexa.chat.application.auth.provided.LoginUseCase;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final LoginUseCase loginUseCase;

    @PostMapping("/login")
    public ResponseEntity<Void> logIn(
        @Valid @RequestBody LoginRequest request,
        @CookieValue(value = "deviceId", required = false) String deviceId,
        HttpServletResponse response
    ) {

        LoginResponse loginResponse = loginUseCase.login(request, deviceId);

        ResponseCookie refreshCookie = createRefreshCookie(loginResponse);
        ResponseCookie accessCookie = createAccessCookie(loginResponse);
        ResponseCookie deviceIdCookie = createDeviceIdCookie(loginResponse);

        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, deviceIdCookie.toString());

        return ResponseEntity.ok().build();
    }

    private ResponseCookie createDeviceIdCookie(LoginResponse loginResponse) {
        return ResponseCookie.from(TokenName.DEVICE_ID.name(), loginResponse.deviceId())
            .httpOnly(true)
            .secure(false)
            .sameSite("Lax")
            .path("/")
            .maxAge(86400L * 365)
            .build();
    }

    private ResponseCookie createAccessCookie(LoginResponse loginResponse) {
        return ResponseCookie.from(TokenName.ACCESS_TOKEN.name(), loginResponse.accessToken())
            .httpOnly(true)
            .secure(false)
            .sameSite("Lax")
            .path("/")
            .maxAge(43200)
            .build();
    }

    private ResponseCookie createRefreshCookie(LoginResponse loginResponse) {
        return ResponseCookie.from(TokenName.REFRESH_TOKEN.name(), loginResponse.refreshToken())
            .httpOnly(true)
            .secure(false)
            .sameSite("Lax")
            .path("/jwt/refresh")
            .maxAge(86400 * 30)
            .build();
    }
}
