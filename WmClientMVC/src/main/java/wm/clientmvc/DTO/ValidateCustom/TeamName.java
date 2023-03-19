package wm.clientmvc.DTO.ValidateCustom;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Pattern(regexp = "^TEAM \\w+", message = "Input must start with 'TEAM ' and have at least one word after it")
public @interface TeamName {
    String message() default "Input must start with 'TEAM ' and have at least one word after it";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}