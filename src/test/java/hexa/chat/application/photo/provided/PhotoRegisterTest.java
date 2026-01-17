package hexa.chat.application.photo.provided;

import hexa.chat.application.member.required.MemberRepository;
import hexa.chat.application.photo.required.PhotoRepository;
import hexa.chat.domain.member.Member;
import hexa.chat.domain.member.MemberFixture;
import hexa.chat.domain.photo.Photo;
import hexa.chat.domain.photo.PhotoRegisterRequest;
import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
class PhotoRegisterTest {

    private final String validBase64 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z8BQDwAEhQGAhKmMIQAAAABJRU5ErkJggg==";

    @Autowired
    private PhotoRegister photoRegister;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager entityManager;

    @DisplayName("회원의 사진을 저장 한다.")
    @Test
    void registerPhoto() {
        // given
        Member member = MemberFixture.createMember();
        memberRepository.save(member);

        PhotoRegisterRequest request = new PhotoRegisterRequest(validBase64);

        // when
        List<Photo> photos = photoRegister.registerPhoto(member.getId(), List.of(request));

        cleanJpaCache();

        // then
        List<Photo> result = photoRepository.findAllByMemberId(member.getId());
        assertThat(result).hasSize(1);

    }

    @DisplayName("존재하지 않는 회원의 사진을 저장하려 하면 예외가 발생한다.")
    @Test
    void registerPhoto_MemberNotFound() {
        // given
        Long invalidMemberId = 99999L;
        PhotoRegisterRequest request = new PhotoRegisterRequest(validBase64);

        // when & then
        assertThatThrownBy(() -> photoRegister.registerPhoto(invalidMemberId, List.of(request)))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessageContaining("회원을 찾을 수 없습니다");
    }

    @DisplayName("유효하지 않은 Base64 이미지로 저장하려 하면 예외가 발생한다.")
    @Test
    void registerPhoto_InvalidImage() {
        // given
        Member member = MemberFixture.createMember();
        memberRepository.save(member);

        String invalidBase64 = "invalid_base64_string_data";
        PhotoRegisterRequest request = new PhotoRegisterRequest(invalidBase64);

        // when & then
        assertThatThrownBy(() -> photoRegister.registerPhoto(member.getId(), List.of(request)))
            .isInstanceOf(ConstraintViolationException.class);
    }

    private void cleanJpaCache() {
        entityManager.flush();
        entityManager.clear();
    }
}