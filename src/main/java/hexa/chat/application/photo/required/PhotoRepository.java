package hexa.chat.application.photo.required;

import hexa.chat.domain.photo.Photo;

import java.util.List;

public interface PhotoRepository {

    Photo save(Photo photo);

    List<Photo> saveAll(List<Photo> photos);

    List<Photo> findAllByMemberId(Long memberId);

}
