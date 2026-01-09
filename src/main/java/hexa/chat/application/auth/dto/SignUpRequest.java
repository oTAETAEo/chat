package hexa.chat.application.auth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import hexa.chat.domain.auth.rule.BirthDateRule;
import hexa.chat.domain.auth.rule.NameRule;
import hexa.chat.domain.auth.rule.PasswordRule;
import hexa.chat.domain.member.MemberRegisterRequest;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record SignUpRequest(

    @NotBlank(message = "필수 요건")
    @Email(message = "이메일 형식에 맞지 않습니다")
    String email,

    String nickname,

    @NameRule
    String name,

    @PasswordRule
    String password,

    @BirthDateRule
    String birthDate

) {

    public MemberRegisterRequest toCommand() {
        return new MemberRegisterRequest(
            email,
            password,
            name,
            nickname,
            LocalDate.parse(birthDate)
        );
    }

}
