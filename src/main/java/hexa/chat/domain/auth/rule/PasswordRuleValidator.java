package hexa.chat.domain.auth.rule;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordRuleValidator implements ConstraintValidator<PasswordRule, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        // 1단계: 비어 있음 체크
        if (value == null || value.isBlank()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("필수 요건")
                .addConstraintViolation();
            return false;
        }

        // 2단계: 길이/패턴 체크
        if (!value.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?~])[A-Za-z\\d!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?~]{8,150}$")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("영문자, 숫자, 특수문자를 포함하여 8자 이상 입니다.")
                .addConstraintViolation();
            return false;
        }

        return true;
    }
}

