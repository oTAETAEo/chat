package hexa.chat.domain.auth.rule;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = BirthDateRuleValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface BirthDateRule {
    String message() default "형식 오류";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
