package gov.mf.rpp.portefeuille.movement.split;

import jakarta.validation.constraints.NotBlank;

/**
 *
 * @author khaled
 */
public record Part(
        @NotBlank(message = "part's name must not be null")
        String name,
        @NotBlank(message = "part's code must not be null or blank")
        String code) {

}
