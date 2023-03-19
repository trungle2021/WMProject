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
@Pattern(regexp = "^([a-zA-Z]{2,}\\s[a-zA-Z]{1,}'?-?[a-zA-Z]{2,}\\s?([a-zA-Z]{1,})?)", message = "Invalid name format. Please enter a name with at least two letters for the first and last name, separated by a space. You may also include an apostrophe or hyphen in the last name.")
public @interface FullName {
    String message() default "Invalid name format. Please enter a name with at least two letters for the first and last name, separated by a space. You may also include an apostrophe or hyphen in the last name.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
