package hexa.chat.application.auth.dto;

public record NameCheckResponse(

    String message,
    boolean result

) {

    public static NameCheckResponse unavailable(String message) {
        return new  NameCheckResponse(
            message,
            true
        );
    }

    public static NameCheckResponse available(String message) {
        return new  NameCheckResponse(
            message,
            false
        );
    }

}
