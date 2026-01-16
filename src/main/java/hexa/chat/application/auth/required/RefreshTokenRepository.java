package hexa.chat.application.auth.required;

import hexa.chat.domain.auth.refreshToken.RefreshToken;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends Repository<RefreshToken, Long> {

    RefreshToken save(RefreshToken refreshToken);

    Optional<RefreshToken> findById(Long id);

    Optional<RefreshToken> findByMemberIdAndDeviceId(Long memberId, String deviceId);

    List<RefreshToken> findAllByMemberId(Long memberId);

    void deleteByMemberIdAndDeviceId(Long memberId, String deviceId);

}
