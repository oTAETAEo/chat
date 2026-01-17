package hexa.chat.domain.photo;

import hexa.chat.domain.member.Member;
import hexa.chat.domain.member.MemberRegisterRequest;
import hexa.chat.domain.member.PasswordEncoder;
import hexa.snowflake.SnowflakeId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Photo {

    @Id
    @SnowflakeId
    private Long id;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String base64Content;

    public static Photo register(PhotoRegisterRequest request, Member member) {
        Photo photo = new Photo();
        photo.member = member;
        photo.base64Content = request.base64Content();
        return photo;
    }

}

