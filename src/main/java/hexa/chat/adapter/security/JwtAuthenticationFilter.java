package hexa.chat.adapter.security;

import hexa.chat.adapter.web.TokenName;
import hexa.chat.application.auth.required.TokenProvider;
import hexa.chat.application.auth.required.TokenProvider.AccessTokenPayload;
import hexa.chat.application.member.provided.MemberFinder;
import hexa.chat.domain.member.Member;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final MemberFinder memberFinder;

    public JwtAuthenticationFilter(TokenProvider tokenProvider, MemberFinder memberFinder) {
        this.tokenProvider = tokenProvider;
        this.memberFinder = memberFinder;
    }

    @Override
    protected void doFilterInternal(
        @NotNull HttpServletRequest request,
        @NotNull HttpServletResponse response,
        @NotNull FilterChain filterChain
    ) throws ServletException, IOException {

        if (SecurityContextHolder.getContext().getAuthentication() == null) {

            Optional.ofNullable(resolveAccessToken(request))
                .flatMap(tokenProvider::parseAccessToken)
                .ifPresent(this::authenticate);
        }

        filterChain.doFilter(request, response);
    }

    private void authenticate(AccessTokenPayload payload) {
        Member member = memberFinder.findByPublicId(payload.publicId());

        MemberPrincipal principal = MemberPrincipal.of(member);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            principal,
            null,
            principal.authorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String resolveAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getCookies()).stream().flatMap(Arrays::stream)
            .filter(c -> TokenName.ACCESS_TOKEN.name().equals(c.getName()))
            .map(Cookie::getValue)
            .findFirst()
            .orElse(null);
    }
}
