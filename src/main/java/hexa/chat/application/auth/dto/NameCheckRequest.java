package hexa.chat.application.auth.dto;

import hexa.chat.domain.auth.rule.NameRule;

public record NameCheckRequest(

    @NameRule
    String name

) {
}
