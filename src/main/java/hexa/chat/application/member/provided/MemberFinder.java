package hexa.chat.application.member.provided;

import hexa.chat.domain.member.Member;
import hexa.chat.domain.shared.Email;
import hexa.chat.domain.shared.Name;

import java.util.List;
import java.util.UUID;

public interface MemberFinder {

    Member findById(Long id);

    Member findByName(String name);

    Member findByEmail(String email);

    Member findByPublicId(UUID publicId);

    List<Member> findAll(List<Long> memberIds);

    List<Member> findAllByPublicId(List<UUID> publicIds);

    boolean existsByEmail(String email);

    boolean existsByName(Name name);
}
