package hexa.chat.domain.shared;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Embeddable
public class SoftDeleteInfo {

    @Column(nullable = false)
    private boolean deleted = false;

    private LocalDateTime deletedAt;

    private Long deletedByMemberId;

    public void delete(Long memberId, LocalDateTime deletedAt) {
        this.deleted = true;
        this.deletedAt = deletedAt;
        this.deletedByMemberId = memberId;
    }

}
