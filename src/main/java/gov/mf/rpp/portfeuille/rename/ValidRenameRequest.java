package gov.mf.rpp.portfeuille.rename;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;

@NotNull
@Constraint(validatedBy = RenameRequestValidator.class)
@Target(PARAMETER)
@Retention(RUNTIME)
@Documented
public @interface ValidRenameRequest {
    
  String message() default "Old name must exist and new name must not be already taken";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
