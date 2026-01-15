package hexa.chat.application.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

    @NotBlank(message = "이메일을 입력 해 주세요.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    String email,

    @NotBlank(message = "비밀번호를 입력 해 주세요.")
    String password

) {
}
