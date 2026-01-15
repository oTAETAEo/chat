package hexa.chat.application.auth.dto;

public record EmailCheckResponse(

    String message,
    boolean result

) {
    public static EmailCheckResponse of(boolean result) {
        if (result){
            return new  EmailCheckResponse(
                "사용중인 이메일 입니다.",
                true
            );
        }
        return new  EmailCheckResponse(
            "멋진 이메일이네요!",
            false
        );
    }
}
