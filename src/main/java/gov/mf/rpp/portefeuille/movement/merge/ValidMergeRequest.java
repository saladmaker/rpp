package gov.mf.rpp.portefeuille.movement.merge;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

/**
 *
 * @author khaled
 */
@Constraint(validatedBy = MergeRequestValidator.class)
@Target({FIELD, PARAMETER, TYPE})
@Retention(RUNTIME)
@Documented
public @interface ValidMergeRequest {
    
  String message() default "merge request must be valid ";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}

