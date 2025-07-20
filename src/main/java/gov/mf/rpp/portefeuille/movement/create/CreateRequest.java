package gov.mf.rpp.portefeuille.movement.create;

import gov.mf.rpp.portefeuille.PortefeuilleStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 *
 * @author khaled
 */
public record CreateRequest(
        @NotBlank(message = "create request's old name must not be null or blank")
        String name,
        @NotBlank(message = "create request's new name must not be null or blank")
        String code,
        @NotNull(message = "create request's status must not be null")
        PortefeuilleStatus status
        ) {

}
