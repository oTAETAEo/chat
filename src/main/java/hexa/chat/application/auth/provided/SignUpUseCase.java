package hexa.chat.application.auth.provided;

import hexa.chat.application.auth.dto.EmailCheckResponse;
import hexa.chat.application.auth.dto.NameCheckResponse;
import hexa.chat.application.auth.dto.SignUpRequest;
import hexa.chat.application.auth.dto.SignUpResponse;
import hexa.chat.domain.shared.Email;
import hexa.chat.domain.shared.Name;
import jakarta.validation.Valid;

public interface SignUpUseCase {

    SignUpResponse signUp(@Valid SignUpRequest request);

    EmailCheckResponse checkEmail(Email email);

    NameCheckResponse checkName(String name);

}
