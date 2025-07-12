package gov.mf.rpp.portefeuille.movement.rename;

import jakarta.validation.constraints.NotBlank;
/**
 * a data carrier for @link rename request
 */
public record RenameRequest(@NotBlank String oldName, @NotBlank String newName) {

}
