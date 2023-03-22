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
@Pattern(regexp = "^TEAM\\s+(?!(?:admin|Admin|ADMIN|ADMINISTRATOR|Administrator|administrator)\\b)[a-zA-Z]+(\\s+[^\\d\\s]+)*$", message = "Input must start with 'TEAM ' and have at least one letter after it and do not contain 'admin or administrator' word.")
public @interface TeamName {
    String message() default "Input must start with 'TEAM ' and have at least one letter after it and do not contain 'admin or administrator' word.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}