package hexa.chat.application.auth.dto;

import hexa.chat.domain.member.rule.NameRule;

public record NameCheckRequest(

    @NameRule
    String name

) {
}
