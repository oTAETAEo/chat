package hexa.chat.application.member;

import hexa.chat.application.member.provided.MemberRegister;
import hexa.chat.application.member.required.MemberRepository;
import hexa.chat.domain.member.*;
import hexa.chat.domain.shared.Email;
import hexa.chat.domain.shared.Name;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class MemberRegisterService implements MemberRegister {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member register(MemberRegisterRequest request) {

        checkDuplicateEmail(request);
        checkDuplicateName(request);

        Member member = Member.register(request, passwordEncoder);

        return memberRepository.save(member);
    }

    private void checkDuplicateName(MemberRegisterRequest request) {
        if (memberRepository.findByName(new Name(request.name())).isPresent()){
            throw new DuplicateNameException("이미 존재하는 이름 입니다 : " + request.name());
        }
    }

    private void checkDuplicateEmail(MemberRegisterRequest request) {
        if (memberRepository.findByEmail(new Email(request.email())).isPresent()){
            throw new DuplicateEmailException("이미 존재하는 이메일 입니다 : " + request.email());
        }
    }

}
