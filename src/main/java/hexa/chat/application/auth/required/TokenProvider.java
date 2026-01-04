package hexa.chat.application.auth.required;

import hexa.chat.domain.shared.Token;
import hexa.chat.domain.member.MemberRole;

import java.util.UUID;

public interface TokenProvider {

    Token createAccessToken(UUID publicId, MemberRole role, long now);

    Token createRefreshToken(UUID publicId, long now);

}
