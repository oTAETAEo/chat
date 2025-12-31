package hexa.chat.application.member.provided;

import hexa.chat.domain.member.Member;
import hexa.chat.domain.member.MemberRegisterRequest;
import jakarta.validation.Valid;

public interface MemberRegister {
    Member register(@Valid MemberRegisterRequest request);
}
