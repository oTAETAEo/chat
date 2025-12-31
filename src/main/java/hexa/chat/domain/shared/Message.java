package hexa.chat.domain.shared;

import jakarta.persistence.Column;

public record Message(
    @Column(nullable = false)
    String content
) {

    public Message {
        if (content == null) {
            throw new IllegalArgumentException("메시지 내용은 비어 있을 수 없습니다.");
        }
    }

}
