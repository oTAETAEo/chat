package hexa.chat.application.auth.provided;

import hexa.chat.application.auth.dto.*;
import hexa.chat.domain.shared.Email;
import hexa.chat.domain.shared.Name;
import jakarta.validation.Valid;

public interface SignUpUseCase {

    SignUpResponse signUp(@Valid SignUpRequest request);

    EmailCheckResponse checkEmail(EmailCheckRequest request);

    NameCheckResponse checkName(NameCheckRequest request);

}
