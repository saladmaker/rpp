package gov.mf.rpp.portefeuille.movement.create;

import gov.mf.rpp.portefeuille.sanity.ValidationSequence;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;
import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.TYPE_USE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

/**
 *
 * @author khaled
 */

@Constraint(validatedBy = CreateRequestValidator.class)
@Target({FIELD, PARAMETER, TYPE, TYPE_USE})
@Retention(RUNTIME)
@Documented
public @interface ValidCreateRequest {

    String message() default  "create request's name and code must not be already taken";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
