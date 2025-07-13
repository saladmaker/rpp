package gov.mf.rpp.portefeuille.movement.split;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;
import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

/**
 *
 * @author khaled
 */
@NotNull(message = "split request must not be null!")
@Constraint(validatedBy = SplitRequestValidator.class)
@Target({ PARAMETER, TYPE })
@Retention(RUNTIME)
@Documented
public @interface ValidSplitRequest {
    
    String message() default "split request must be valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
