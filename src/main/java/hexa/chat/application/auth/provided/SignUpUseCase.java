package hexa.chat.application.auth.provided;

import hexa.chat.application.auth.dto.EmailCheckResponse;
import hexa.chat.application.auth.dto.SignUpRequest;
import hexa.chat.application.auth.dto.SignUpResponse;
import jakarta.validation.Valid;

public interface SignUpUseCase {

    SignUpResponse signUp(@Valid SignUpRequest request);

    EmailCheckResponse checkEmail(String email);
}
