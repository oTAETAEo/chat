package hexa.chat.adapter.security.oauth2;

import hexa.chat.domain.member.MemberRole;

import java.util.UUID;

public record MemberDto (

    UUID publicId,

    MemberRole role

) {

}