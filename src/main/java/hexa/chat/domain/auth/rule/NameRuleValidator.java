package hexa.chat.domain.auth.rule;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NameRuleValidator implements ConstraintValidator<NameRule, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null || value.isBlank()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("필수 요건")
                .addConstraintViolation();
            return false;
        }

        if (value.length() < 2 || value.length() > 50){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("이름은 2자 이상 50자 이하여야 합니다")
                .addConstraintViolation();
            return false;
        }

        if (!value.matches("^[a-zA-Z_]+$")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("이름은 영문자와 언더스코어만 포함할 수 있습니다")
                .addConstraintViolation();
            return false;
        }

        return true;
    }
}
