package hexa.chat.domain.friendship;

public class DuplicateFriendShipException extends RuntimeException {
    public DuplicateFriendShipException(String message) {
        super(message);
    }
}
