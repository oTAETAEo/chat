package hexa.chat.domain.auth.rule;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class BirthDateRuleValidator implements ConstraintValidator<BirthDateRule, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null || value.isBlank()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("생년월일은 필수입니다")
                .addConstraintViolation();
            return false;
        }

        if (!value.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("생년월일 형식이 올바르지 않습니다 (형식: yyyy-MM-dd, 예: 2000-01-01)")
                .addConstraintViolation();
            return false;
        }

        LocalDate birthDate;
        try {
            birthDate = LocalDate.parse(value);
        } catch (DateTimeParseException e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("유효하지 않은 날짜입니다")
                .addConstraintViolation();
            return false;
        }

        if (birthDate.isAfter(LocalDate.now())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("생년월일은 과거 날짜여야 합니다")
                .addConstraintViolation();
            return false;
        }

        LocalDate minAgeDate = LocalDate.now().minusYears(14);
        if (birthDate.isAfter(minAgeDate)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("14세 이상만 가입할 수 있습니다")
                .addConstraintViolation();
            return false;
        }

        LocalDate maxAgeDate = LocalDate.now().minusYears(150);
        if (birthDate.isBefore(maxAgeDate)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("생년월일이 유효하지 않습니다")
                .addConstraintViolation();
            return false;
        }

        return true;
    }
}