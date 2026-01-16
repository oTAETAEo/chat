package hexa.chat.adapter.jpa.member;

import hexa.chat.application.member.required.MemberRepository;
import hexa.chat.domain.member.Member;
import hexa.chat.domain.shared.Email;
import hexa.chat.domain.shared.Name;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryAdapter implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;

    @Override
    public Member save(Member member) {
        return memberJpaRepository.save(member);
    }

    @Override
    public List<Member> saveAll(List<Member> members) {
        return memberJpaRepository.saveAll(members);
    }

    @Override
    public Optional<Member> findByEmail(Email email) {
        return memberJpaRepository.findMemberByEmail(email);
    }

    @Override
    public Optional<Member> findByName(Name name) {
        return memberJpaRepository.findMemberByName(name);
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        return memberJpaRepository.findById(memberId);
    }

    @Override
    public Optional<Member> findByPublicId(UUID publicId) {
        return memberJpaRepository.findMemberByPublicId(publicId);
    }

    @Override
    public List<Member> findAllById(List<Long> memberIds) {
        return memberJpaRepository.findAllById(memberIds);
    }

    @Override
    public List<Member> findAllByPublicId(List<UUID> publicIds) {
        return memberJpaRepository.findAllByPublicIdIn(publicIds);
    }

    @Override
    public boolean existsByEmail(Email email) {
        return memberJpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByName(Name name) {
        return memberJpaRepository.existsByName(name);
    }

}
