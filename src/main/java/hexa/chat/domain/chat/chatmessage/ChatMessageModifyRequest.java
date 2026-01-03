package hexa.chat.domain.chat.chatmessage;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChatMessageModifyRequest(

    @NotNull
    Long chatMessageId,

    @NotBlank
    String newMessage

) {
}
