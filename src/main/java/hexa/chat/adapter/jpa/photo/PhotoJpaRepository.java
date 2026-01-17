package hexa.chat.adapter.jpa.photo;

import hexa.chat.domain.member.Member;
import hexa.chat.domain.photo.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoJpaRepository extends JpaRepository<Photo, Long> {

    List<Photo> findAllByMemberId(Long memberId);
}
