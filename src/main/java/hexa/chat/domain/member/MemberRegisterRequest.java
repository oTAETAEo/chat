package hexa.chat.domain.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record MemberRegisterRequest(

    @Email
    String email,

    @Size(min = 8, max = 30)
    String password,

    @Size(min = 1, max = 20)
    String name,

    @Size(max = 20)
    String nickname,

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate birthDate
) {
}
