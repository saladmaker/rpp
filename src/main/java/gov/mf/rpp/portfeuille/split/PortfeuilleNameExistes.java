package gov.mf.rpp.portfeuille.split;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.CONSTRUCTOR;

@Constraint(validatedBy = PortfeuilleSplitRequestValidator.class)
@Target({ METHOD, CONSTRUCTOR })
@Retention(RUNTIME)
@Documented
public @interface PortfeuilleNameExistes {
  String message() default "End date must be after begin date and both must be in the future";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
