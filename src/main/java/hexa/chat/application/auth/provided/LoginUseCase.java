package hexa.chat.application.auth.provided;

import hexa.chat.application.auth.dto.LoginRequest;
import hexa.chat.application.auth.dto.LoginResponse;
import jakarta.annotation.Nullable;

public interface LoginUseCase {

    LoginResponse login(LoginRequest request, @Nullable String deviceId);

}
