package hexa.chat.application.member;

import hexa.chat.application.member.dto.MemberInfoPublicResponse;
import hexa.chat.application.member.provided.MemberFinder;
import hexa.chat.application.member.provided.MemberQuery;
import hexa.chat.application.member.required.MemberRepository;
import hexa.chat.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberQueryService implements MemberQuery {

    private final MemberRepository memberRepository;

    @Override
    public MemberInfoPublicResponse memberPublicInfo(Long id) {

        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        return MemberInfoPublicResponse.of(member);
    }
}
