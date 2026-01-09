package hexa.chat.domain.member;

import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;

public class MemberFixture {

    public static Member createMember(){
        return Member.register(createMemberRegisterRequest(), createPasswordEncoder());
    }

    public static Member createMember(Long id){
        Member member = Member.register(createMemberRegisterRequest(), createPasswordEncoder());
        ReflectionTestUtils.setField(member, "id", id);
        return member;
    }

    public static Member createMember(String email, String name, String nickname){
        return Member.register(createMemberRegisterRequest(email, name, nickname), createPasswordEncoder());
    }

    public static Member createMember(Long id, String email, String name, String nickname){
        Member member = Member.register(createMemberRegisterRequest(email, name, nickname), createPasswordEncoder());
        ReflectionTestUtils.setField(member, "id", id);
        return member;
    }

    public static Member createMember(String email, String password, PasswordEncoder encoder){
        return Member.register(createMemberRegisterRequest(email, password), encoder);
    }

    public static MemberRegisterRequest createMemberRegisterRequest(String email, String password){
        return new MemberRegisterRequest(email, password, "test", "otesto", LocalDate.of(2025, 1,1));
    }

    public static MemberRegisterRequest createMemberRegisterRequest(){
        return new MemberRegisterRequest("test@test.com", "test1234", "test", "otesto", LocalDate.of(2025, 1,1));
    }

    public static MemberRegisterRequest createMemberRegisterRequest(String email, String name, String nickname){
        return new MemberRegisterRequest(email, "test1234", name, nickname, LocalDate.of(2025, 1,1));
    }

    public static PasswordEncoder createPasswordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(String password) {
                return password.toUpperCase();
            }

            @Override
            public boolean matches(String password, String passwordHash) {
                return encode(password).equals(passwordHash);
            }
        };
    }
}

