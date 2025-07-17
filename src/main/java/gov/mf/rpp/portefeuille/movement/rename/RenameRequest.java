package gov.mf.rpp.portefeuille.movement.rename;

import gov.mf.rpp.portefeuille.sanity.ValidationSequence;
import jakarta.validation.constraints.NotBlank;

/**
 * a data carrier for @link rename request
 */

@ValidRenameRequest(groups = ValidationSequence.BusinessCheck.class)
public record RenameRequest(
        @NotBlank(message = "rename request's old name must not be null or blank",
                groups = ValidationSequence.SanityCheck.class) String oldName,
        @NotBlank(message = "rename request's new name must not be null or blank",
                groups = ValidationSequence.SanityCheck.class) String newName) {

}
