package gov.mf.rpp.portefeuille.movement.create;

import gov.mf.rpp.portefeuille.PortefeuilleStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 *
 * @author khaled
 */

@ValidCreateRequest
public record CreateRequest(
        @NotBlank String name,
        @NotBlank String code,
        @NotNull PortefeuilleStatus status) {
}
