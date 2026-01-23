package hexa.chat.adapter.security.oauth2;

import hexa.chat.adapter.security.JwtTokenProvider;
import hexa.chat.adapter.web.TokenName;
import hexa.chat.domain.shared.Token;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        MemberDto memberDto = customUserDetails.getMemberDto();

        long now = System.currentTimeMillis();
        Token accessToken = jwtTokenProvider.createAccessToken(memberDto.publicId(), memberDto.role(), now);

        ResponseCookie cookie = createAccessCookie(accessToken.value());

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        response.sendRedirect("http://localhost:3000/");
    }

    private ResponseCookie createAccessCookie(String token) {
        return ResponseCookie.from(TokenName.ACCESS_TOKEN.name(), token)
            .httpOnly(true)
            .secure(false)
            .sameSite("Lax")
            .path("/")
            .maxAge(43200)
            .build();
    }

}