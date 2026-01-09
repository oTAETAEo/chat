package hexa.chat.domain.member;

import hexa.chat.domain.AbstractEntity;
import hexa.chat.domain.shared.Email;
import hexa.chat.domain.shared.Name;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends AbstractEntity {

    @Column(unique = true, nullable = false)
    private UUID publicId;

    @NaturalId
    @Embedded
    private Email email;

    @Column(nullable = false)
    private String passwordHash;

    @Embedded
    private Name name;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private MemberRole role;

    public static Member register(MemberRegisterRequest request, PasswordEncoder passwordEncoder){
        Member member = new Member();

        member.email = new Email(Objects.requireNonNull(request.email()));
        member.name = new Name(Objects.requireNonNull(request.name()));
        member.passwordHash = passwordEncoder.encode(Objects.requireNonNull(request.password()));
        member.birthDate = Objects.requireNonNull(request.birthDate());
        member.nickname = resolveNickname(request);
        member.publicId = UUID.randomUUID();
        member.role = MemberRole.USER;

        return member;
    }

    public boolean verifyPassword(String password, PasswordEncoder passwordEncoder){
        return passwordEncoder.matches(password, this.passwordHash);
    }

    public void changeName(String name){
        this.name = new Name(name);
    }

    public void changeNickname(String nickname){
        this.nickname = nickname;
    }

    public void changeBirthDate(LocalDate birthDate){
        this.birthDate = birthDate;
    }

    public void changePassword(String password, PasswordEncoder passwordEncoder){
        this.passwordHash = passwordEncoder.encode(password);
    }

    private static String resolveNickname(MemberRegisterRequest request) {
        String nickname = request.nickname();
        if (nickname == null || nickname.isBlank()) {
            nickname = request.name();
        }
        return nickname;
    }

}
