package hexa.chat.adapter.security;

import org.springframework.security.core.GrantedAuthority;

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
}
