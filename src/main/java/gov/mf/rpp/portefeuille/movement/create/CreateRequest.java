package gov.mf.rpp.portefeuille.movement.create;

import gov.mf.rpp.portefeuille.PortefeuilleStatus;
import gov.mf.rpp.portefeuille.sanity.ValidationSequence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 *
 * @author khaled
 */
@ValidCreateRequest(groups = ValidationSequence.BusinessCheck.class)
public record CreateRequest(
        @NotBlank(message = "create request's old name must not be null or blank",
                groups = ValidationSequence.SanityCheck.class)
        String name,
        @NotBlank(message = "create request's new name must not be null or blank",
                groups = ValidationSequence.SanityCheck.class)
        String code,
        @NotNull(message = "create request's status must not be null",
                groups = ValidationSequence.SanityCheck.class)
        PortefeuilleStatus status) {

}
