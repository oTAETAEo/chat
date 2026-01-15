package hexa.chat.adapter.jpa.member;

import hexa.chat.domain.member.Member;
import hexa.chat.domain.shared.Email;
import hexa.chat.domain.shared.Name;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    Optional<Member> findMemberByEmail(Email email);

    Optional<Member> findMemberByName(Name name);

    Optional<Member> findMemberByPublicId(UUID publicId);

    List<Member> findAllByPublicIdIn(List<UUID> publicIds);

    boolean existsByEmail(Email email);
}
