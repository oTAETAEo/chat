package hexa.chat.application.auth.dto;

import hexa.chat.domain.member.Member;
import hexa.chat.domain.member.MemberRegisterRequest;
import hexa.chat.domain.member.rule.BirthDateRule;
import hexa.chat.domain.member.rule.NameRule;
import hexa.chat.domain.member.rule.PasswordRule;
import hexa.chat.domain.photo.PhotoRegisterRequest;
import hexa.chat.domain.photo.rule.ValidBase64Image;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public record SignUpRequest(
    @Valid AccountInfo account,
    @Valid ProfileInfo profile,
    @Valid PhotoInfo photos,
    @Valid AdditionalInfo additional
) {

    // Step 1: 계정 정보
    public record AccountInfo(
        @NotBlank(message = "필수 요건") @Email(message = "이메일 형식에 맞지 않습니다") String email,
        @PasswordRule String password
    ) {}

    // Step 2: 기본 프로필
    public record ProfileInfo(
        @NameRule String name,
        @NotBlank String nickname,
        @BirthDateRule String birthDate
    ) {}

    // Step 3: 사진
    public record PhotoInfo(
        @Size(min = 2, max = 5, message = "사진은 2장 이상 5장 이하로 등록해야 합니다")
        List<@ValidBase64Image String> urls
    ) {}

    // Step 4: 부가 정보
    public record AdditionalInfo(
        @NotNull String location,
        @NotNull List<String> interests,
        @NotNull String aboutMe
    ) {}

    public MemberRegisterRequest toMemberRegisterCommand() {
        return new MemberRegisterRequest(
            account.email(),
            account.password(),
            profile.name(),
            profile.nickname(),
            LocalDate.parse(profile.birthDate())
        );
    }

    public List<PhotoRegisterRequest> toPhotoRegisterCommands() {
        return photos.urls().stream()
            .map(PhotoRegisterRequest::new)
            .toList();
    }

}