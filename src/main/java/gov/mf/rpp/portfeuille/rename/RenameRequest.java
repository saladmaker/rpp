package gov.mf.rpp.portfeuille.rename;

import jakarta.validation.constraints.NotBlank;
/**
 * a data carrier for @link rename request
 */
public record RenameRequest(@NotBlank String oldName, @NotBlank String newName) {

}
