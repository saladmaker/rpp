package gov.mf.rpp.portefeuille.movement.rename;

import gov.mf.rpp.portefeuille.sanity.ValidationSequence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
/**
 * a data carrier for @link rename request
 */
@NotNull(
        message = "rename request must not be null",
        groups = ValidationSequence.SanityCheck.class
)
@ValidRenameRequest(groups = ValidationSequence.BusinessCheck.class)
public record RenameRequest(
        @NotBlank(message = "rename request's old name must not be null or blank",
                groups = ValidationSequence.SanityCheck.class) String oldName,
        @NotBlank(message = "rename request's new name must not be null or blank",
                groups = ValidationSequence.SanityCheck.class) String newName) {

}
