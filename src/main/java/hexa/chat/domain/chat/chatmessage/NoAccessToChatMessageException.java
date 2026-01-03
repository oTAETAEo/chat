package hexa.chat.domain.chat.chatmessage;

public class NoAccessToChatMessageException extends RuntimeException {
    public NoAccessToChatMessageException(String message) {
        super(message);
    }
}
