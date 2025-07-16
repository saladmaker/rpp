package gov.mf.rpp.portefeuille.movement.rename;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = RenameRequestValidator.class)
@Target({FIELD, PARAMETER, TYPE})
@Retention(RUNTIME)
@Documented
public @interface ValidRenameRequest {
    
  String message() default "Old name must exist and new name must not be already taken";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
