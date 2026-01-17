package hexa.chat.application.auth.provided;

import hexa.chat.application.auth.dto.SignUpRequest;

import java.util.List;

public class SignUpFixture {

    private static final String validBase64 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z8BQDwAEhQGAhKmMIQAAAABJRU5ErkJggg==";
    public static SignUpRequest createSignUpRequest() {
        return new SignUpRequest(
            new SignUpRequest.AccountInfo("test@test.com", "test@1234"),
            new SignUpRequest.ProfileInfo("test", "_test_", "2011-01-10"),
            new SignUpRequest.PhotoInfo(List.of(validBase64, validBase64)),
            new SignUpRequest.AdditionalInfo("Seoul", List.of("Coding"), "Hello!")
        );
    }
}
