package hexa.chat.application.member.provided;

import hexa.chat.domain.member.Member;

import java.util.List;
import java.util.UUID;

public interface MemberFinder {

    Member find(Long id);

    Member find(String name);

    List<Member> findAll(List<Long> memberIds);

    List<Member> findAllByPublicId(List<UUID> publicIds);

}

