package hexa.chat.application.member.required;

import hexa.chat.domain.member.Member;
import hexa.chat.domain.shared.Email;
import hexa.chat.domain.shared.Name;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberRepository {

    Member save(Member member);

    List<Member> saveAll(List<Member> members);

    Optional<Member> findByEmail(Email email);

    Optional<Member> findByName(Name name);

    Optional<Member> findById(Long memberId);

    List<Member> findAllById(List<Long> memberIds);

    List<Member> findAllByPublicId(List<UUID> publicIds);
}
