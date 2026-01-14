package hexa.chat.application.auth;

import hexa.chat.application.auth.dto.LoginRequest;
import hexa.chat.application.auth.dto.LoginResponse;
import hexa.chat.application.auth.dto.SignUpRequest;
import hexa.chat.application.auth.dto.SignUpResponse;
import hexa.chat.application.auth.provided.LoginUseCase;
import hexa.chat.application.auth.provided.SignUpUseCase;
import hexa.chat.application.auth.required.RefreshTokenRepository;
import hexa.chat.application.auth.required.TokenProvider;
import hexa.chat.application.member.provided.MemberFinder;
import hexa.chat.application.member.provided.MemberRegister;
import hexa.chat.domain.auth.InvalidCredentialsException;
import hexa.chat.domain.auth.refreshToken.RefreshToken;
import hexa.chat.domain.member.Member;
import hexa.chat.domain.member.PasswordEncoder;
import hexa.chat.domain.shared.Token;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Date;
import java.util.UUID;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class AuthService implements LoginUseCase , SignUpUseCase {

    private final PasswordEncoder passwordEncoder;

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    private final MemberFinder memberFinder;
    private final MemberRegister memberRegister;

    @Override
    public LoginResponse login(@Valid LoginRequest request, @Nullable String deviceId) {

        Member member = memberFinder.findByEmail(request.email());

        if (!member.verifyPassword(request.password(), passwordEncoder)) {
            throw new InvalidCredentialsException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        long now = nowTime();
        String resolveDeviceId = resolveDeviceId(deviceId);
        Token accessJwt = tokenProvider.createAccessToken(member.getPublicId(), member.getRole(), now);
        Token refreshJwt = tokenProvider.createRefreshToken(member.getPublicId(), now);

        RefreshToken refreshToken = refreshTokenRepository
            .findByMemberIdAndDeviceId(member.getId(), resolveDeviceId)
            .map(token -> token.rotate(refreshJwt))
            .orElseGet(() -> RefreshToken.register(member.getId(), resolveDeviceId, refreshJwt));
        refreshTokenRepository.save(refreshToken);

        return LoginResponse.of(accessJwt.value(), refreshJwt.value(), resolveDeviceId);
    }

    @Override
    public SignUpResponse signUp(@Valid SignUpRequest request) {

        Member member = memberRegister.register(request.toCommand());

        return SignUpResponse.of(member);
    }

    private long nowTime() {
        return new Date().getTime();
    }

    private String resolveDeviceId(String deviceId) {
        return (deviceId == null || deviceId.isBlank())
            ? UUID.randomUUID().toString()
            : deviceId;
    }
}
