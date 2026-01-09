package hexa.chat.application.member;

import hexa.chat.application.member.dto.MemberInfoPublicResponse;
import hexa.chat.application.member.provided.MemberFinder;
import hexa.chat.application.member.provided.MemberQuery;
import hexa.chat.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberQueryService implements MemberQuery {

    private final MemberFinder memberFinder;

    @Override
    public MemberInfoPublicResponse memberPublicInfo(Long id) {

        Member member = memberFinder.findById(id);

        return MemberInfoPublicResponse.of(member);
    }
}
