package hexa.chat.application.auth.dto;

import hexa.chat.domain.member.Member;

public record SignUpResponse(

    String welcomeMessage

) {

    public static SignUpResponse of(Member member){
        return new SignUpResponse(
            member.getName().name() + " 님 가입을 환영합니다."
        );
    }

}
