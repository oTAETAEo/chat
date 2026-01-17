package hexa.chat.application.photo;

import hexa.chat.application.member.provided.MemberFinder;
import hexa.chat.application.photo.provided.PhotoRegister;
import hexa.chat.application.photo.required.PhotoRepository;
import hexa.chat.domain.member.Member;
import hexa.chat.domain.photo.Photo;
import hexa.chat.domain.photo.PhotoRegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class PhotoRegisterService implements PhotoRegister {

    private final MemberFinder memberFinder;

    private final PhotoRepository photoRepository;

    @Override
    public List<Photo> registerPhoto(Long memberId,List<@Valid PhotoRegisterRequest> requests) {

        Member member = memberFinder.findById(memberId);

        List<Photo> photos = requests.stream()
            .map(request -> Photo.register(request, member))
            .toList();

        return photoRepository.saveAll(photos);
    }

}
