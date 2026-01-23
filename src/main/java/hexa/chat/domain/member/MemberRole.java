package hexa.chat.domain.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {


    GUEST("GUEST"),
    USER ("USER"),
    ADMIN("ADMIN");

    private final String role;

}
