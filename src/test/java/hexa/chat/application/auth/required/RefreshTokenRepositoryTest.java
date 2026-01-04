package hexa.chat.application.auth.required;

import hexa.chat.domain.auth.refreshToken.RefreshToken;
import hexa.chat.domain.shared.Token;
import hexa.config.TestQueryDslConfig;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestQueryDslConfig.class)
class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private EntityManager entityManager;

    @DisplayName("Refresh Token을 저장한다.")
    @Test
    void save() {
        // given
        RefreshToken refreshToken = RefreshToken.register(1L, "ipad1234", new Token("1234.1234.1234", new Date()));

        // when
        refreshTokenRepository.save(refreshToken);

        entityManager.flush();
        entityManager.clear();

        // then
        Optional<RefreshToken> result = refreshTokenRepository.findById(refreshToken.getId());
        assertThat(result).isPresent();
        assertThat(result.get().getTokenValue()).isEqualTo(refreshToken.getTokenValue());
    }

}