package hexa.chat.application.auth.provided;

public interface LogOutUseCase {

    void logOut(Long memberId, String deviceId);
}
