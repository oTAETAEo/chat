package hexa.chat.application.member.provided;

import hexa.chat.application.member.dto.MemberInfoPublicResponse;

public interface MemberQuery {

    MemberInfoPublicResponse memberPublicInfo(Long id);

}
