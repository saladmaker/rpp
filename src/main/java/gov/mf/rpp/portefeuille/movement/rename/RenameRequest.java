package gov.mf.rpp.portefeuille.movement.rename;

import jakarta.validation.constraints.NotBlank;
/**
 * a data carrier for @link rename request
 */
public record RenameRequest(
        @NotBlank(message = "rename request's old name must not be null or blank") String oldName,
        @NotBlank(message = "rename request's new name must not be null or blank") String newName) {

}
