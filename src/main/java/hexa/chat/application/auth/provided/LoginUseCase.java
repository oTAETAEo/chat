package hexa.chat.application.auth.provided;

import hexa.chat.application.auth.dto.LoginRequest;
import hexa.chat.application.auth.dto.LoginResponse;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;

public interface LoginUseCase {

    LoginResponse login(@Valid LoginRequest request, @Nullable String deviceId);

}
