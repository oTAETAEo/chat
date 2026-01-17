package hexa.chat.application.auth.provided;

import hexa.chat.application.auth.dto.*;
import hexa.chat.application.member.provided.MemberFinder;
import hexa.chat.application.member.provided.MemberRegister;
import hexa.chat.application.photo.provided.PhotoRegister;
import hexa.chat.domain.member.Member;
import hexa.chat.domain.photo.Photo;
import hexa.chat.domain.photo.PhotoRegisterRequest;
import hexa.chat.domain.shared.Email;
import hexa.chat.domain.shared.Name;
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
public class SignUpUseCaseService implements SignUpUseCase {

    private final MemberFinder memberFinder;
    private final MemberRegister memberRegister;

    private final PhotoRegister photoRegister;

    @Override
    public SignUpResponse signUp(@Valid SignUpRequest request) {

        Member member = memberRegister.register(request.toMemberRegisterCommand());

        List<Photo> photo = photoRegister.registerPhoto(member.getId(), request.toPhotoRegisterCommands());

        return SignUpResponse.of(member);
    }

    @Override
    public EmailCheckResponse checkEmail(EmailCheckRequest request) {

        boolean result = memberFinder.existsByEmail(new Email(request.email()));

        return EmailCheckResponse.of(result);
    }

    @Override
    public NameCheckResponse checkName(NameCheckRequest request) {

        if (!Name.isValid(request.name())) {
            return NameCheckResponse.unavailable("이름은 특수문자를 포함할 수 없습니다.");
        }

        if (memberFinder.existsByName(new Name(request.name()))) {
            return NameCheckResponse.unavailable("멋진 다른 이름을 골라보세요!");
        }

        return NameCheckResponse.available("사용 가능한 이름입니다.");
    }
}
