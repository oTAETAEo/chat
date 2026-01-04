package hexa.chat.domain.auth.rule;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordRuleValidator implements ConstraintValidator<PasswordRule, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        // 1단계: 비어 있음 체크
        if (value == null || value.isBlank()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("비밀번호를 입력 해 주세요.")
                .addConstraintViolation();
            return false;
        }

        // 2단계: 길이/패턴 체크
        if (!value.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("영문자, 숫자를 포함하여 8자 이상 입니다.")
                .addConstraintViolation();
            return false;
        }

        return true;
    }
}

