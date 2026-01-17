package hexa.chat.adapter.jpa.photo;

import hexa.chat.application.photo.required.PhotoRepository;
import hexa.chat.domain.photo.Photo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PhotoRepositoryAdapter implements PhotoRepository {

    private final PhotoJpaRepository photoJpaRepository;

    @Override
    public Photo save(Photo photo) {
        return photoJpaRepository.save(photo);
    }

    @Override
    public List<Photo> saveAll(List<Photo> photos) {
        return photoJpaRepository.saveAll(photos);
    }

    @Override
    public List<Photo> findAllByMemberId(Long memberId) {
        return photoJpaRepository.findAllByMemberId(memberId);
    }
}
