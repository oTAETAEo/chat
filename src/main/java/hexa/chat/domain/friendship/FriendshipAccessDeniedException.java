package hexa.chat.domain.friendship;

public class FriendshipAccessDeniedException extends RuntimeException {
    public FriendshipAccessDeniedException(String message) {
        super(message);
    }
}
