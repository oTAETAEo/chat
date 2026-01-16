package hexa.chat.domain.shared;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.regex.Pattern;

@Embeddable
public record Name(
    @Column(nullable = false, unique = true)
    String name
) {
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z_]+$");

    public Name {
        if (!isValid(name)) {
            throw new IllegalArgumentException("이름 형식에 맞지 않습니다 : " + name);
        }
    }

    public static boolean isValid(String name) {
        return name != null && NAME_PATTERN.matcher(name).matches();
    }
}