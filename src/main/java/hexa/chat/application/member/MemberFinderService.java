package hexa.chat.application.member;

import hexa.chat.application.member.provided.MemberFinder;
import hexa.chat.application.member.required.MemberRepository;
import hexa.chat.domain.member.Member;
import hexa.chat.domain.shared.Name;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberFinderService implements MemberFinder {

    private final MemberRepository memberRepository;

    @Override
    public Member find(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new NoSuchElementException("회원을 찾을 수 없습니다."));
    }

    @Override
    public Member find(String memberName) {
        return memberRepository.findByName(new Name(memberName))
            .orElseThrow();
    }

    @Override
    public List<Member> findAll(List<Long> memberIds) {
        return memberRepository.findAllById(memberIds);
    }

    @Override
    public List<Member> findAllByPublicId(List<UUID> memberIds) {
        return memberRepository.findAllByPublicId(memberIds);
    }

}
