package hexa.chat.domain.shared;

import java.util.Date;

public record Token(
    String value,
    Date validity
) {

    public Token {
        validity = new Date(validity.getTime());
    }

    @Override
    public Date validity() {
        return new Date(validity.getTime());
    }
}
