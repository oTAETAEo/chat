package hexa.chat.adapter.web.auth;

import hexa.chat.adapter.security.MemberPrincipal;
import hexa.chat.adapter.web.TokenName;
import hexa.chat.adapter.web.dto.AuthInfoResponse;
import hexa.chat.adapter.web.dto.LoginInfoResponse;
import hexa.chat.application.auth.dto.*;
import hexa.chat.application.auth.provided.LogOutUseCase;
import hexa.chat.application.auth.provided.LoginUseCase;
import hexa.chat.application.auth.provided.SignUpUseCase;
import hexa.chat.application.friendship.dto.FriendshipInfoResponse;
import hexa.chat.application.friendship.provided.FriendshipQuery;
import hexa.chat.application.member.dto.MemberInfoPublicResponse;
import hexa.chat.application.member.provided.MemberQuery;
import hexa.chat.domain.shared.Email;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final SignUpUseCase signUpUseCase;
    private final LogOutUseCase logOutUseCase;

    private final MemberQuery memberQuery;
    private final FriendshipQuery friendshipQuery;

    @PostMapping("/api/auth/login")
    public ResponseEntity<LoginInfoResponse> logIn(
        @Valid @RequestBody LoginRequest request,
        @CookieValue(value = "DEVICE_ID" , required = false) String deviceId,
        HttpServletResponse response
    ) {

        LoginResponse loginResponse = loginUseCase.login(request, deviceId);

        ResponseCookie refreshCookie = createRefreshCookie(loginResponse);
        ResponseCookie accessCookie = createAccessCookie(loginResponse);
        ResponseCookie deviceIdCookie = createDeviceIdCookie(loginResponse);

        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, deviceIdCookie.toString());

        return ResponseEntity.ok().body(LoginInfoResponse.of(loginResponse));
    }

    @PostMapping("/api/auth/sign-up")
    public ResponseEntity<SignUpResponse> signUp(@Valid @RequestBody SignUpRequest request) {

        SignUpResponse response = signUpUseCase.signUp(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/auth/logout")
    public ResponseEntity<Void> logOut(
        @AuthenticationPrincipal MemberPrincipal memberPrincipal,
        @CookieValue(value = "DEVICE_ID" , required = false) String deviceId
    ) {

        logOutUseCase.logOut(memberPrincipal.id(), deviceId);

        log.info("UserId {} logged out", memberPrincipal.id());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/auth/check-email")
    public ResponseEntity<EmailCheckResponse> checkEmail(@RequestParam("email") String email) {

        EmailCheckResponse response = signUpUseCase.checkEmail(new Email(email));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/auth/check-name")
    public ResponseEntity<NameCheckResponse> checkName(@RequestParam("name") String name) {

        NameCheckResponse response = signUpUseCase.checkName(name);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/auth/info")
    public ResponseEntity<AuthInfoResponse> memberInfo(@AuthenticationPrincipal MemberPrincipal memberPrincipal){

        MemberInfoPublicResponse memberPublicInfo = memberQuery.memberPublicInfo(memberPrincipal.id());
        FriendshipInfoResponse memberFriendshipInfo = friendshipQuery.memberFriendshipInfo(memberPrincipal.id());

        return ResponseEntity.ok(
            AuthInfoResponse.of(memberPublicInfo, memberFriendshipInfo)
        );
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
