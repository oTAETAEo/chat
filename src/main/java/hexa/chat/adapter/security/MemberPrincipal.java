package hexa.chat.adapter.security;

import hexa.chat.domain.member.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

public record MemberPrincipal(
    Long id,
    Collection<? extends GrantedAuthority> authorities
) implements Principal {

    public MemberPrincipal {
        authorities = List.copyOf(authorities);
    }
    @Override
    public String getName() {
        return id.toString();
    }

    public static MemberPrincipal of(Member member) {
        return new MemberPrincipal(
            member.getId(),
            List.of(new SimpleGrantedAuthority("ROLE_" + member.getRole().name())));
    }

}
