package hexa.chat.application.photo.provided;

import hexa.chat.domain.photo.Photo;
import hexa.chat.domain.photo.PhotoRegisterRequest;
import jakarta.validation.Valid;

import java.util.List;

public interface PhotoRegister {

    List<Photo> registerPhoto(Long memberId, List<@Valid PhotoRegisterRequest> requests);

}
