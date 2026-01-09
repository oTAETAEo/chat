package hexa.chat.application.member.dto;

import hexa.chat.domain.member.Member;

import java.util.UUID;

public record MemberInfoPublicResponse(
    UUID publicId,
    String email,
    String name,
    String nickname
) {

    public static MemberInfoPublicResponse of(Member member){
        return new MemberInfoPublicResponse(
            member.getPublicId(),
            member.getEmail().address(),
            member.getName().name(),
            member.getNickname()
        );
    }
}
