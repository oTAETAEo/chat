package hexa.chat.domain.photo;

import hexa.chat.domain.photo.rule.ValidBase64Image;

public record PhotoRegisterRequest(

    @ValidBase64Image
    String base64Content

) {

}
